package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.exceptions.AnimeAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.AnimeRepository;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.GenreService;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimeServiceImpl implements AnimeService {
    private final AnimeRepository animeRepository;
    private final ModelMapper modelMapper;
    private final GenreService genreService;
    private final SeasonService seasonService;
    private final ValidatorUtil validatorUtil;
    private final CollectionMapperUtil collectionMapperUtil;

    @Autowired
    public AnimeServiceImpl(AnimeRepository animeRepository,
                            ModelMapper modelMapper,
                            GenreService genreService,
                            SeasonService seasonService,
                            ValidatorUtil validatorUtil,
                            CollectionMapperUtil collectionMapperUtil) {
        this.animeRepository = animeRepository;
        this.modelMapper = modelMapper;
        this.genreService = genreService;
        this.seasonService = seasonService;
        this.validatorUtil = validatorUtil;
        this.collectionMapperUtil = collectionMapperUtil;
    }

    private Sort createSort(String type) throws IllegalArgumentException {
        Sort sort = null;
        switch (type) {
            case "MOST_POPULAR":
                sort = Sort.by("likes").descending();
                break;

            case "NEW_RELEASES":
                sort = Sort.by("releaseDate").descending();
                break;

            case "RANDOMIZED":
                sort = Sort.unsorted();
                break;

            default:
                throw new IllegalArgumentException(String.format("Type %s is not supported!", type));
        }

        return sort;
    }

    private Set<Genre> getAnimeGenres(AnimeServiceModel animeServiceModel) {
        return animeServiceModel
                .getGenres()
                .stream()
                .map(genreServiceModel -> this.genreService.fetchByName(genreServiceModel.getName()))
                .map(genreServiceModel -> this.modelMapper.map(genreServiceModel, Genre.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void persist(AnimeServiceModel animeServiceModel) {
        try {
            AnimeServiceModel byTitle = this.fetchByTitle(animeServiceModel.getName());
            throw new AnimeAlreadyExistsException(String.format("Anime %s already exists!", byTitle.getName()));
        } catch (EntityNotFoundException ignored) { }

        animeServiceModel.setReleaseDate(LocalDate.now());
        animeServiceModel.setCompleted(false);
        animeServiceModel.setLikes(0);

//        TODO move create message logic to the ValidatorUtil class
        if (!this.validatorUtil.isValid(animeServiceModel)) {
            StringBuilder fullErrorMessage = new StringBuilder();

            Set<ConstraintViolation<AnimeServiceModel>> violations = this.validatorUtil.violations(animeServiceModel);
            violations
                    .forEach(animeConstraintViolation -> {
                        fullErrorMessage.append(animeConstraintViolation.getMessage());
                        fullErrorMessage.append(System.lineSeparator());
                    });

            throw new IllegalArgumentException(fullErrorMessage.toString());
        }

        Set<Genre> genres = this.getAnimeGenres(animeServiceModel);
        Anime anime = this.modelMapper.map(animeServiceModel, Anime.class);
        anime.setGenres(genres);

        AnimeServiceModel createdAnime
                = this.modelMapper.map(this.animeRepository.saveAndFlush(anime), AnimeServiceModel.class);

        this.seasonService.persist(createdAnime);
    }

    @Override
    public void delete(Long id) {
        AnimeServiceModel animeServiceModel = this.fetchById(id);
        this.animeRepository.deleteById(animeServiceModel.getId());
    }

    @Override
    public boolean isEmptyRepository() {
        return this.animeRepository.count() == 0;
    }

    @Override
    public AnimeServiceModel likeByTitle(String title, String username) throws IllegalStateException {
        AnimeServiceModel animeServiceModel = this.fetchByTitle(title);

        if (animeServiceModel.getLikedBy() == null) {
            animeServiceModel.setLikedBy(new HashSet<String>());
        }

        if (animeServiceModel.getLikes() == null) {
            animeServiceModel.setLikes(0);
        }

        if (animeServiceModel.getLikedBy().contains(username)) {
            throw new IllegalStateException();
        }

        animeServiceModel.getLikedBy().add(username);
        animeServiceModel.like();

        Anime anime = this.animeRepository.saveAndFlush(this.modelMapper.map(animeServiceModel, Anime.class));
        return this.modelMapper.map(anime, AnimeServiceModel.class);
    }

    @Override
    public AnimeServiceModel fetchById(Long id) throws EntityNotFoundException {
        Anime anime = this.animeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Anime with id %d has not been found!", id)));

        return this.modelMapper.map(anime, AnimeServiceModel.class);
    }

    @Override
    public AnimeServiceModel fetchByTitle(String title) {
        Anime anime = this.animeRepository
                .findByNameIgnoreCase(title)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Anime %s does not exists!", title)));

        return this.modelMapper.map(anime, AnimeServiceModel.class);
    }

    @Override
    public List<AnimeServiceModel> fetchFirstTenMostPopular() throws NoSuchElementException {
        List<Anime> animes = this.animeRepository.findFirst10ByOrderByLikesDesc();

        if (animes.isEmpty()) {
            throw new NoSuchElementException("There are not any animes uploaded yet!");
        }

        return this.collectionMapperUtil.map(animes, AnimeServiceModel.class);
    }

    @Override
    public List<AnimeServiceModel> fetchFirstTenNewlyReleased() throws NoSuchElementException {
        List<Anime> animes = this.animeRepository.findFirst10ByOrderByReleaseDateDesc();

        if (animes.isEmpty()) {
            throw new NoSuchElementException("There are not any animes uploaded yet!");
        }

        return this.collectionMapperUtil.map(animes, AnimeServiceModel.class);
    }

    @Override
    public List<AnimeServiceModel> fetchFirstTenRandom() throws NoSuchElementException {
        if (this.isEmptyRepository()) {
            throw new NoSuchElementException("There are not any animes uploaded yet!");
        }

        List<Anime> all = this.animeRepository.findAll();

        List<AnimeServiceModel> shuffledAnimes = this.collectionMapperUtil.map(all, AnimeServiceModel.class);

        Collections.shuffle(shuffledAnimes);

        int count = Math.min(shuffledAnimes.size(), 10);

        if (count < 10) {
            return shuffledAnimes;
        } else {
            return shuffledAnimes.subList(0, 11);
        }
    }

    @Override
    public List<AnimeServiceModel> fetchAllOfTypeByPage(String type, String pageNumberAsString) throws IllegalArgumentException {
        Sort sort = this.createSort(type);

        int pageNumber = Integer.parseInt(pageNumberAsString);
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sort);

        Page<Anime> sortedAnimesPage = this.animeRepository.findAll(pageable);

        List<Anime> sortedAnimesList = new ArrayList<>();
        if (type.equals("RANDOMIZED")) {
            for (Anime anime : sortedAnimesPage) {
                sortedAnimesList.add(anime);
            }

            Collections.shuffle(sortedAnimesList);
        } else {
            sortedAnimesList = sortedAnimesPage.getContent();
        }

        return this.collectionMapperUtil.map(sortedAnimesList, AnimeServiceModel.class);
    }

    @Override
    public List<AnimeServiceModel> fetchAllStartsWithSearchValue(String searchValue) {
        List<Anime> foundAnimes = this.animeRepository.findByNameStartsWithIgnoreCase(searchValue);
        return this.collectionMapperUtil.map(foundAnimes, AnimeServiceModel.class);
    }

    @Override
    public Set<AnimeServiceModel> fetchAllWithGenre(String genre) {
        Set<Anime> allWithGenre = this.animeRepository.findAllWithGenre(genre);
        return new HashSet<>(this.collectionMapperUtil.map(allWithGenre, AnimeServiceModel.class));
//        .stream()
//                .map(anime -> this.modelMapper.map(anime, AnimeServiceModel.class))
//                .collect(Collectors.toSet());
    }
}

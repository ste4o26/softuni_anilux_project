package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.entities.Season;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.exceptions.AnimeAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.AnimeRepository;
import anilux.anilux_spring_mvc.repositories.SeasonRepository;
import anilux.anilux_spring_mvc.services.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnimeServiceTest extends BaseTest {
    Anime anime;

    @MockBean
    SeasonRepository seasonRepository;

    @MockBean
    AnimeRepository animeRepository;

    @Autowired
    AnimeService service;

    @BeforeEach
    void setup() {
        anime = new Anime();
        anime.setId(1L);
        anime.setName("Boku No Hero Academia");
        anime.setSummary("Some summery just in case...");
        anime.setImageThumbnailUrl("/img/2.jpg");
        anime.setReleaseDate(LocalDate.of(2021, 1, 27));
//        anime.setCompleted(false);
        anime.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        Genre genre = new Genre();
        genre.setName("Shounen");
        anime.setGenres(Set.of(genre));

        Mockito.when(animeRepository.findById(anime.getId()))
                .thenReturn(Optional.of(anime));

        Mockito.when(animeRepository.saveAndFlush(anime))
                .thenReturn(anime);

        Mockito.lenient().when(animeRepository.findByNameIgnoreCase(anime.getName()))
                .thenReturn(Optional.of(anime));
    }

    @AfterEach
    public void reset() {
        Mockito.reset(animeRepository, seasonRepository);
    }

    @Test
    public void delete_whenValidIdIsProvided_shouldSuccessfullyDeleteTheAnime() {
        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);

        service.delete(animeServiceModel.getId());

        Mockito.verify(animeRepository, Mockito.times(1))
                .deleteById(animeServiceModel.getId());

        Mockito.when(seasonRepository.saveAndFlush(Mockito.any(Season.class)))
                .thenReturn(new Season());

        Mockito.lenient().when(animeRepository.findByNameIgnoreCase("Boku No Hero Academia"))
                .thenReturn(Optional.of(anime));
    }

    @Test
    public void persist_whenIsValidAnime_shouldPersistSuccessfully() {
        Mockito.lenient().when(animeRepository.findByNameIgnoreCase(anime.getName()))
                .thenThrow(EntityNotFoundException.class);

        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);

        service.persist(animeServiceModel);

        Mockito.verify((animeRepository), Mockito.times(1))
                .saveAndFlush(anime);
    }

    @Test
    public void persist_whenIsINVALIDAnime_shouldThrowException() {
        AnimeServiceModel animeServiceModel = new AnimeServiceModel();
        assertThrows(IllegalArgumentException.class, () -> service.persist(animeServiceModel));
    }

    @Test
    public void persist_whenAnimeAlreadyExists_shouldThrowException() {
        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);
        assertThrows(AnimeAlreadyExistsException.class, () -> service.persist(animeServiceModel));
    }

    @Test
    public void isEmptyRepository_whenRepositoryIsEmpty_shouldReturnTrue() {
        Mockito.when(this.animeRepository.count())
                .thenReturn(0L);

        boolean actual = service.isEmptyRepository();

        assertTrue(actual);
    }

    @Test
    public void isEmptyRepository_whenRepositoryIsNOTEmpty_shouldReturnFalse() {
        Mockito.when(this.animeRepository.count())
                .thenReturn(10L);

        boolean actual = service.isEmptyRepository();

        assertFalse(actual);
    }

    @Test
    public void likeByTitle_whenValidTitleAndUserHasNotLikeTheAnime_shouldLikeSuccessfully() {
        AnimeServiceModel animeServiceModel = service.likeByTitle("Boku No Hero Academia", "testUser");

        ArgumentCaptor<Anime> argument = ArgumentCaptor.forClass(Anime.class);
        Mockito.verify(animeRepository).saveAndFlush(argument.capture());
        Anime actual = argument.getValue();

        assertTrue(actual.getLikedBy().contains("testUser"));
        assertEquals(1, actual.getLikedBy().size());
    }

    @Test
    public void likeByTitle_whenValidTitleAndUserHasAlreadyLikedTheAnime_shouldThrowException() {
        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);
        animeServiceModel.setLikedBy(Set.of("testUser"));

        Mockito.when(animeRepository.findByNameIgnoreCase(anime.getName()))
                .thenReturn(Optional.of(modelMapper.map(animeServiceModel, Anime.class)));

        assertThrows(IllegalStateException.class, () -> service.likeByTitle(anime.getName(), "testUser"));
    }

    private List<Anime> setupListOfAnimes() {
        List<Anime> animes = new ArrayList<>();

        Genre genre = new Genre();
        genre.setName("Shounen");

        Anime anime3 = new Anime();
        anime3.setId(3L);
        anime3.setName("Black Clover");
        anime3.setSummary("Some summery just in case...");
        anime3.setImageThumbnailUrl("/img/2.jpg");
        anime3.setReleaseDate(LocalDate.of(2021, 3, 20));
//        anime3.setCompleted(false);
        anime3.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        anime3.setLikes(18);
        anime3.setGenres(Set.of(genre));
        animes.add(anime3);

        anime.setLikes(10);
        animes.add(anime);


        Anime anime2 = new Anime();
        anime2.setId(2L);
        anime2.setName("Naruto");
        anime2.setSummary("Some summery just in case...");
        anime2.setImageThumbnailUrl("/img/2.jpg");
        anime2.setReleaseDate(LocalDate.of(2020, 8, 15));
//        anime2.setCompleted(false);
        anime2.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        anime2.setLikes(3);
        anime2.setGenres(Set.of(genre));
        animes.add(anime2);

        return animes;
    }

    @Test
    public void fetchFirstTenMostPopular_whenAnimesPresentInDB_shouldReturnUpToTenSortedByLikes() {
        List<Anime> animes = this.setupListOfAnimes();

        Mockito.when(animeRepository.findFirst10ByOrderByLikesDesc())
                .thenReturn(animes);

        List<AnimeServiceModel> actual = service.fetchFirstTenMostPopular();

        assertEquals(3, actual.size());
        assertEquals(anime.getName(), actual.get(1).getName());
        assertTrue((actual.get(0).getLikes() >= actual.get(1).getLikes()) && (actual.get(1).getLikes() >= actual.get(2).getLikes()));
    }

    @Test
    public void fetchFirstTenMostPopular_whenIsEmptyDB_shouldThrowException() {
        List<Anime> animes = new ArrayList<>();

        Mockito.when(animeRepository.findFirst10ByOrderByLikesDesc())
                .thenReturn(animes);

        assertThrows(NoSuchElementException.class, () -> service.fetchFirstTenMostPopular());
    }

    @Test
    public void fetchFirstTenNewlyReleased_whenAnimesPresentInDB_shouldReturnUpToTenSortedByReleaseData() {
        List<Anime> animes = this.setupListOfAnimes();

        Mockito.when(animeRepository.findFirst10ByOrderByReleaseDateDesc())
                .thenReturn(animes);

        List<AnimeServiceModel> actual = service.fetchFirstTenNewlyReleased();

        assertEquals(3, actual.size());
        assertEquals(anime.getName(), actual.get(1).getName());
        assertTrue(actual.get(0).getReleaseDate().isAfter(actual.get(1).getReleaseDate()) &&
                actual.get(1).getReleaseDate().isAfter(actual.get(2).getReleaseDate()));
    }

    @Test
    public void fetchFirstTenNewlyReleased_whenIsEmptyDB_shouldThrowException() {
        List<Anime> animes = new ArrayList<>();

        Mockito.when(animeRepository.findFirst10ByOrderByReleaseDateDesc())
                .thenReturn(animes);

        assertThrows(NoSuchElementException.class, () -> service.fetchFirstTenNewlyReleased());
    }

    @Test
    public void fetchAllOfTypeByPage_whenTypeIsMOST_POPULAR_shouldReturnTheGivenPageOfAnimes() {
        PageImpl<Anime> animesPage = new PageImpl<Anime>(this.setupListOfAnimes());

        Mockito.when(animeRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(animesPage);

        List<AnimeServiceModel> actual = service.fetchAllOfTypeByPage("MOST_POPULAR", "1");

        assertEquals(3, actual.size());
        assertEquals("Black Clover", actual.get(0).getName());
        assertTrue((actual.get(0).getLikes() >= actual.get(1).getLikes()) && (actual.get(1).getLikes() >= actual.get(2).getLikes()));
    }

    @Test
    public void fetchAllOfTypeByPage_whenTypeIsNEW_RELEASES_shouldReturnTheGivenPageOfAnimes() {
        PageImpl<Anime> animesPage = new PageImpl<Anime>(this.setupListOfAnimes());

        Mockito.when(animeRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(animesPage);

        List<AnimeServiceModel> actual = service.fetchAllOfTypeByPage("NEW_RELEASES", "1");

        assertEquals(3, actual.size());
        assertEquals("Black Clover", actual.get(0).getName());
        assertTrue(actual.get(0).getReleaseDate().isAfter(actual.get(1).getReleaseDate()) &&
                actual.get(1).getReleaseDate().isAfter(actual.get(2).getReleaseDate()));
    }

    @Test
    public void fetchAllOfTypeByPage_whenTypeIsINVALID_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.fetchAllOfTypeByPage("INVALID", "1"));
    }

    @Test
    public void fetchAllStartsWithSearchValue_whenAreMatchesCaseInsensitive_shouldReturnAllOfThem() {
        List<Anime> animes = this.setupListOfAnimes();
        animes.remove(2);
        Mockito.when(animeRepository.findByNameStartsWithIgnoreCase("b"))
                .thenReturn(animes);

        List<AnimeServiceModel> actual = service.fetchAllStartsWithSearchValue("b");

        assertEquals(2, actual.size());
        assertTrue(actual.get(0).getName().charAt(0) == 'b' || actual.get(0).getName().charAt(0) == 'B');
        assertTrue(actual.get(1).getName().charAt(0) == 'b' || actual.get(1).getName().charAt(0) == 'B');
    }

    @Test
    public void fetchAllStartsWithSearchValue_whenAreNOMatchesCaseInsensitive_shouldReturnEmptyList() {
        List<Anime> animes = new ArrayList<>();
        Mockito.when(animeRepository.findByNameStartsWithIgnoreCase("b"))
                .thenReturn(animes);

        List<AnimeServiceModel> actual = service.fetchAllStartsWithSearchValue("b");

        assertEquals(0, actual.size());
    }

    @Test
    public void fetchAllWithGenre_whenPresentAnimeWithAGivenGenre_shouldReturnThemAll() {
        Mockito.when(animeRepository.findAllWithGenre("Shounen"))
                .thenReturn(new HashSet<>(this.setupListOfAnimes()));

        GenreServiceModel genre = new GenreServiceModel("Shounen");

        Set<AnimeServiceModel> actual = service.fetchAllWithGenre("Shounen");

        assertEquals(3, actual.size());
        actual.forEach(anime -> assertTrue(anime.getGenres().contains(genre)));
    }

}
package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.GenreViewModel;
import anilux.anilux_spring_mvc.exceptions.GenreAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.GenreRepository;
import anilux.anilux_spring_mvc.services.interfaces.GenreService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final CollectionMapperUtil collectionMapperUtil;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil, CollectionMapperUtil collectionMapperUtil) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.collectionMapperUtil = collectionMapperUtil;
    }

    @Override
    public void persist(GenreServiceModel genreServiceModel) {
        try {
            GenreServiceModel byName = this.fetchByName(genreServiceModel.getName());
            throw new GenreAlreadyExistsException(String.format("Genre %s already exists!", byName.getName()));
        } catch (EntityNotFoundException ignored) {
        }

        if (!this.validatorUtil.isValid(genreServiceModel)) {
            Set<ConstraintViolation<GenreServiceModel>> violations = this.validatorUtil.violations(genreServiceModel);

            StringBuilder finalErrorMessage = new StringBuilder();
            violations.forEach(genreServiceModelViolation -> finalErrorMessage.append(genreServiceModelViolation.getMessage()));

            throw new IllegalArgumentException(finalErrorMessage.toString());
        }

        Genre genre = this.modelMapper.map(genreServiceModel, Genre.class);
        this.genreRepository.saveAndFlush(genre);
    }

    @Override
    public boolean isEmpty() {
        return this.genreRepository.count() == 0;
    }

    @Override
    public GenreServiceModel fetchById(Long id) throws EntityNotFoundException {
        Genre genre = this.genreRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Genre with id %d does nto exists!", id)));

        return this.modelMapper.map(genre, GenreServiceModel.class);
    }

    @Override
    public GenreServiceModel fetchByName(String name) {
        Genre genre = this.genreRepository
                .findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Genre %s does not exists!", name)));

        return this.modelMapper.map(genre, GenreServiceModel.class);
    }

    @Override
    public List<GenreViewModel> fetchAllSortedByName() {
        List<Genre> genresNames = this.genreRepository.findAll(Sort.by("name"));
        return this.collectionMapperUtil.map(genresNames, GenreViewModel.class);
    }
}

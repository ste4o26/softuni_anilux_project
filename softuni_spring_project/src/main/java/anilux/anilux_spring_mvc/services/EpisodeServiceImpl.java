package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Episode;
import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;
import anilux.anilux_spring_mvc.repositories.EpisodeRepository;
import anilux.anilux_spring_mvc.services.interfaces.EpisodeService;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final ModelMapper modelMapper;
    private final SeasonService seasonService;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public EpisodeServiceImpl(EpisodeRepository episodeRepository, ModelMapper modelMapper, SeasonService seasonService, ValidatorUtil validatorUtil) {
        this.episodeRepository = episodeRepository;
        this.modelMapper = modelMapper;
        this.seasonService = seasonService;
        this.validatorUtil = validatorUtil;
    }

    public void persist(EpisodeServiceModel episodeServiceModel) {
        episodeServiceModel.setReleaseDate(LocalDate.now());

        SeasonServiceModel seasonServiceModel =
                this.seasonService.fetchById(episodeServiceModel.getSeason().getId());

        if (seasonServiceModel.getEpisodes() == null) {
            seasonServiceModel.setEpisodes(new LinkedHashSet<>());
        }

        episodeServiceModel.setSeason(seasonServiceModel);
        episodeServiceModel.setNumber(seasonServiceModel.getEpisodes().size() + 1);

        if (!this.validatorUtil.isValid(episodeServiceModel)) {
            Set<ConstraintViolation<EpisodeServiceModel>> violations =
                    this.validatorUtil.violations(episodeServiceModel);

            StringBuilder finalErrorMassage = new StringBuilder();
            violations.forEach(episodeServiceModelViolation -> finalErrorMassage.append(episodeServiceModelViolation.getMessage()));

            throw new IllegalArgumentException(finalErrorMassage.toString());
        }

        Episode episode = this.modelMapper.map(episodeServiceModel, Episode.class);
        this.episodeRepository.saveAndFlush(episode);
    }

    @Override
    public EpisodeServiceModel fetchById(Long id) throws EntityNotFoundException {
        Episode episode = this.episodeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Episode with id %d does not exists!", id)));

        return this.modelMapper.map(episode, EpisodeServiceModel.class);
    }

    @Override
    public EpisodeServiceModel fetchBySeasonIdAndNumber(Long seasonId, Integer number) throws EntityNotFoundException {
        Episode episode = this.episodeRepository
                .findBySeasonIdAndNumber(seasonId, number)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Episode number %d does not exists!", number)));

        return this.modelMapper.map(episode, EpisodeServiceModel.class);
    }
}

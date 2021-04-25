package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Season;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;
import anilux.anilux_spring_mvc.repositories.SeasonRepository;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class SeasonServiceImpl implements SeasonService {
    private final SeasonRepository seasonRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public SeasonServiceImpl(SeasonRepository seasonRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.seasonRepository = seasonRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public void persist(AnimeServiceModel animeServiceModel) {
        if (!this.validatorUtil.isValid(animeServiceModel)) {
            Set<ConstraintViolation<AnimeServiceModel>> violations =
                    this.validatorUtil.violations(animeServiceModel);

            StringBuilder finalErrorMassage = new StringBuilder();
            violations.forEach(animeServiceModelViolation -> finalErrorMassage.append(animeServiceModelViolation.getMessage()));

            throw new IllegalArgumentException(finalErrorMassage.toString());
        }

        Anime anime = this.modelMapper.map(animeServiceModel, Anime.class);

        Season season = new Season();
        if (anime.getSeasons() == null) {
            anime.setSeasons(new LinkedHashSet<>());
            season.setNumber(1);
        } else {
            season.setNumber(anime.getSeasons().size() + 1);
        }

        season.setAnime(anime);
        season.setCompleted(false);
        season.setEpisodes(new HashSet<>());

        this.seasonRepository.saveAndFlush(season);
    }

    @Override
    public SeasonServiceModel delete(Long seasonId) {
        SeasonServiceModel seasonServiceModel = this.fetchById(seasonId);

        this.seasonRepository.deleteById(seasonId);

        return seasonServiceModel;
    }

    @Override
    public SeasonServiceModel fetchById(Long id) throws EntityNotFoundException {
        Season season = this.seasonRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Season with %d does not exists!", id)));

        return this.modelMapper.map(season, SeasonServiceModel.class);
    }

    @Override
    public SeasonServiceModel completeById(Long seasonId) {
        SeasonServiceModel seasonServiceModel = this.fetchById(seasonId);
        if (seasonServiceModel.isCompleted()) {
            throw new IllegalStateException("This season is already completed!");
        }

        seasonServiceModel.setCompleted(true);


        Season season = this.modelMapper.map(seasonServiceModel, Season.class);
        this.seasonRepository.saveAndFlush(season);

        return seasonServiceModel;
    }
}

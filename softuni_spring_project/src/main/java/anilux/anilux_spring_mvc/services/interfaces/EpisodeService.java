package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;

public interface EpisodeService {

    void persist(EpisodeServiceModel episodeServiceModel);

    EpisodeServiceModel fetchById(Long id);

    EpisodeServiceModel fetchBySeasonIdAndNumber(Long seasonId, Integer number);
}

package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;

public interface SeasonService {
    void persist(AnimeServiceModel animeServiceModel);

    SeasonServiceModel delete(Long seasonId);

    SeasonServiceModel fetchById(Long id);
}

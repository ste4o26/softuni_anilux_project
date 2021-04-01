package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.view_models.UserViewModel;

public interface RecommendationService {
    UserViewModel recommendAnimes(String username);
}

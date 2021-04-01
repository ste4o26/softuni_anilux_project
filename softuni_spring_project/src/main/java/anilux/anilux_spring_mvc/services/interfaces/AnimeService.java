package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;

import java.util.List;
import java.util.Set;

public interface AnimeService {
    void persist(AnimeServiceModel animeServiceModel);

    void delete(Long id);

    boolean isEmptyRepository();

    AnimeServiceModel likeByTitle(String title, String username);

    AnimeServiceModel fetchById(Long id);

    AnimeServiceModel fetchByTitle(String title);

    List<AnimeServiceModel> fetchFirstTenMostPopular();

    List<AnimeServiceModel> fetchFirstTenNewlyReleased();

    List<AnimeServiceModel> fetchFirstTenRandom();

    List<AnimeServiceModel> fetchAllOfTypeByPage(String type, String pageNumberAsString);

    List<AnimeServiceModel> fetchAllStartsWithSearchValue(String searchValue);

    Set<AnimeServiceModel> fetchAllWithGenre(String genre);
}

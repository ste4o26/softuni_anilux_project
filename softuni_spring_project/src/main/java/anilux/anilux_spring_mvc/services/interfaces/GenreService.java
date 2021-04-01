package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.GenreViewModel;

import java.util.List;

public interface GenreService {
    void persist(GenreServiceModel genreServiceModel);

    boolean isEmpty();

    GenreServiceModel fetchById(Long id);

    GenreServiceModel fetchByName(String name);

    List<GenreViewModel> fetchAllSortedByName();
}

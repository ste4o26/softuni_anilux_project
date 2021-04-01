package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.entities.User;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.UserAdminViewModel;

import java.util.List;

public interface UserService {

    UserServiceModel updateRoles(String userToBeUpdateUsername, boolean forPromotion);

    UserServiceModel register(UserServiceModel userServiceModel);

    List<User> deleteAnimeFromAllUsersList(AnimeServiceModel animeServiceModel);

    void addToMyAnimeList(String title, String username);

    UserServiceModel fetchByUsername(String username);

    List<UserAdminViewModel> fetchAll();

    List<AnimeServiceModel> fetchUserList(String username);
}

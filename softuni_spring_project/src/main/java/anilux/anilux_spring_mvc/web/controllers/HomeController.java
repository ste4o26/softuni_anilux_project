package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.AnimeViewModel;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final AnimeService animeService;
    private final UserService userService;
    private final CollectionMapperUtil collectionMapperUtil;


    @Autowired
    public HomeController(AnimeService animeService,
                          UserService userService,
                          CollectionMapperUtil collectionMapperUtil) {
        this.animeService = animeService;
        this.userService = userService;
        this.collectionMapperUtil = collectionMapperUtil;
    }


    @GetMapping
    public String index(Principal principal) {
        if (principal != null && principal.getName() != null) {
            return "redirect:/users/login";
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        List<AnimeServiceModel> mostPopular = this.animeService.fetchFirstTenMostPopular();
        List<AnimeServiceModel> newlyReleased = this.animeService.fetchFirstTenNewlyReleased();
        List<AnimeServiceModel> random = this.animeService.fetchFirstTenRandom();
        List<AnimeServiceModel> myList = this.userService.fetchUserList(principal.getName());

        model.addAttribute("mostPopular", this.collectionMapperUtil.map(mostPopular, AnimeViewModel.class));
        model.addAttribute("newlyReleased", this.collectionMapperUtil.map(newlyReleased, AnimeViewModel.class));
        model.addAttribute("random", this.collectionMapperUtil.map(random, AnimeViewModel.class));
        model.addAttribute("myList", myList);

        return "home";
    }
}

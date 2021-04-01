package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.SeasonViewModel;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seasons")
public class SeasonController {
    private final SeasonService seasonService;
    private final AnimeService animeService;
    private final ModelMapper modelMapper;
    private final CollectionMapperUtil collectionMapperUtil;

    @Autowired
    public SeasonController(SeasonService seasonService, AnimeService animeService, ModelMapper modelMapper, CollectionMapperUtil collectionMapperUtil) {
        this.seasonService = seasonService;
        this.animeService = animeService;
        this.modelMapper = modelMapper;
        this.collectionMapperUtil = collectionMapperUtil;
    }

    @GetMapping("/{id}")
    public String season(@PathVariable("id") Long id, Model model) {
        SeasonServiceModel seasonServiceModel = this.seasonService.fetchById(id);
        SeasonViewModel seasonViewModel = this.modelMapper.map(seasonServiceModel, SeasonViewModel.class);

        List<String> genresNames = this.collectionMapperUtil.map(seasonViewModel.getAnime().getGenres(), String.class);

        seasonViewModel.sortEpisodes(seasonServiceModel.getEpisodes(), this.modelMapper);

        model.addAttribute("anime", seasonViewModel.getAnime());
        model.addAttribute("season", seasonViewModel);
        model.addAttribute("genres", String.join(", ", genresNames));

        return "season";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/add/{animeId}")
    public String postAdd(@PathVariable("animeId") Long animeId) {
        AnimeServiceModel animeServiceModel = this.animeService.fetchById(animeId);
        this.seasonService.persist(animeServiceModel);

        return String.format("redirect:/animes/%d", animeId);
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String postDelete(Long seasonId) {
        SeasonServiceModel seasonServiceModel = this.seasonService.delete(seasonId);

        return String.format("redirect:/animes/%d", seasonServiceModel.getAnime().getId());
    }
}

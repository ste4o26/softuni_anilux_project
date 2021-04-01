package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.binding_models.EpisodeBindingModel;
import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.EpisodeViewModel;
import anilux.anilux_spring_mvc.services.interfaces.CloudinaryService;
import anilux.anilux_spring_mvc.services.interfaces.EpisodeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public EpisodeController(EpisodeService episodeService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.episodeService = episodeService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/{seasonId}")
    public String getById(@PathVariable("seasonId") Long seasonId,
                          @RequestParam(value = "number") Integer number, Model model) {
        EpisodeViewModel episodeViewModel =
                this.modelMapper.map(this.episodeService.fetchBySeasonIdAndNumber(seasonId, number), EpisodeViewModel.class);

        model.addAttribute("episode", episodeViewModel);

        return "episode";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/add/{seasonId}")
    public String add(@PathVariable("seasonId") Long seasonId, Model model) {
        if (!model.containsAttribute("episodeBindingModel")) {
            EpisodeBindingModel episodeBindingModel = new EpisodeBindingModel();
            episodeBindingModel.setSeasonId(seasonId);
            model.addAttribute("episodeBindingModel", episodeBindingModel);
        }

        if (!model.containsAttribute("hasVideo")) {
            model.addAttribute("hasVideo", true);
        }

        return "add-episode";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/add/{seasonId}")
    public String postAdd(@PathVariable("seasonId") Long seasonId,
                          @Valid @ModelAttribute("episodeBindingModel")
                                  EpisodeBindingModel episodeBindingModel,
                          RedirectAttributes redirectAttributes) throws IOException {

        String originalFilename = episodeBindingModel.getEpisode().getOriginalFilename();
        if (episodeBindingModel.getEpisode() == null || originalFilename.equals("")) {
            redirectAttributes.addFlashAttribute("hasVideo", false);
            return String.format("redirect:/episodes/add/%d", seasonId);
        } else {
            redirectAttributes.addFlashAttribute("hasVideo", true);
        }

        String videoUrl = this.cloudinaryService.uploadVideo(episodeBindingModel.getEpisode());

        EpisodeServiceModel episodeServiceModel = this.modelMapper.map(episodeBindingModel, EpisodeServiceModel.class);
        episodeServiceModel.setEpisodeUrl(videoUrl);
        episodeServiceModel.setId(null);

        this.episodeService.persist(episodeServiceModel);

        return String.format("redirect:/seasons/%d", seasonId);
    }
}

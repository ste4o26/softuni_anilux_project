package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.binding_models.GenreBindingModel;
import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.services.interfaces.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreController(GenreService genreService, ModelMapper modelMapper) {
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/upload")
    public String upload(Model model) {
        if (!model.containsAttribute("genreBindingModel")) {
            model.addAttribute("genreBindingModel", new GenreBindingModel());
        }

        return "add-genre";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public String postUpload(@Valid @ModelAttribute("genreBindingModel")
                                  GenreBindingModel genreBindingModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("genreBindingModel", genreBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.genreBindingModel", bindingResult);
            return "redirect:/genres/upload";
        }

        GenreServiceModel genreServiceModel = this.modelMapper.map(genreBindingModel, GenreServiceModel.class);
        this.genreService.persist(genreServiceModel);

        return "redirect:/animes/upload";
    }
}

package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.binding_models.AnimeBindingModel;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.DetailAnimeViewModel;
import anilux.anilux_spring_mvc.domain.view_models.GenreViewModel;
import anilux.anilux_spring_mvc.domain.view_models.AnimeViewModel;
import anilux.anilux_spring_mvc.exceptions.AnimeAlreadyExistsException;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.CloudinaryService;
import anilux.anilux_spring_mvc.services.interfaces.GenreService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/animes")
public class AnimeController {
    private final AnimeService animeService;
    private final ModelMapper modelMapper;
    private final GenreService genreService;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final CollectionMapperUtil collectionMapperUtil;

    @Autowired
    public AnimeController(AnimeService animeService,
                           ModelMapper modelMapper,
                           GenreService genreService,
                           CloudinaryService cloudinaryService,
                           UserService userService,
                           CollectionMapperUtil collectionMapperUtil) {
        this.animeService = animeService;
        this.modelMapper = modelMapper;
        this.genreService = genreService;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
        this.collectionMapperUtil = collectionMapperUtil;
    }

    @GetMapping("/{animeId}")
    public String getById(@PathVariable("animeId") Long animeId, Model model) {
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "");
        }

        DetailAnimeViewModel detailAnimeViewModel =
                this.modelMapper.map(this.animeService.fetchById(animeId), DetailAnimeViewModel.class);

        detailAnimeViewModel.sortSeasonsBySeasonNumber();

        List<String> genresNames = this.collectionMapperUtil.map(detailAnimeViewModel.getGenres(), String.class);

        model.addAttribute("anime", detailAnimeViewModel);
        model.addAttribute("genres", String.join(", ", genresNames));

        return "anime";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/upload")
    public String upload(Model model) {
        if (!model.containsAttribute("animeBindingModel")) {
            model.addAttribute("animeBindingModel", new AnimeBindingModel());
            model.addAttribute("hasImage", true);
        }

        List<GenreViewModel> genres = this.genreService.fetchAllSortedByName();
        model.addAttribute("genres", genres);

        return "add-anime";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public String postUpload(@Valid @ModelAttribute("animeBindingModel")
                                     AnimeBindingModel animeBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {

        String originalFilename = animeBindingModel.getImage().getOriginalFilename();
        if (bindingResult.hasErrors() || (animeBindingModel.getImage() == null || originalFilename.equals(""))) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.animeBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("animeBindingModel", animeBindingModel);
            redirectAttributes.addFlashAttribute("hasImage", false);
            return "redirect:/animes/upload";
        } else {
            redirectAttributes.addFlashAttribute("hasImage", true);
        }

        String imageUrl = this.cloudinaryService.uploadImage(animeBindingModel.getImage());

        AnimeServiceModel animeServiceModel = this.modelMapper.map(animeBindingModel, AnimeServiceModel.class);
        animeServiceModel.setImageThumbnailUrl(imageUrl);

        try {
            this.animeService.persist(animeServiceModel);
        } catch (AnimeAlreadyExistsException aaee) {
            redirectAttributes.addFlashAttribute("errorMassage", aaee.getMessage());
        }

        return "redirect:/animes/upload";
    }

    @GetMapping("/all")
    public String getAllByPage(@RequestParam("type") String type,
                               @RequestParam("pageNumber") String pageNumberAsString,
                               Model model) {
        int pageNumber = Integer.parseInt(pageNumberAsString);

        List<AnimeServiceModel> sortedAnimeServiceModels =
                this.animeService.fetchAllOfTypeByPage(type, pageNumberAsString);

        List<AnimeViewModel> sortedAnimeViewModels =
                this.collectionMapperUtil.map(sortedAnimeServiceModels, AnimeViewModel.class);

        model.addAttribute("allAnimes", sortedAnimeViewModels);
        model.addAttribute("animesType", type);
        model.addAttribute("pageNum", pageNumber);

        return "all-animes";
    }


    @GetMapping("/byTitle")
    public String getByTitle(@RequestParam("title") String title,
                             @RequestParam(value = "message", required = false) String message,
                             RedirectAttributes redirectAttributes) {
        AnimeServiceModel animeServiceModel = this.animeService.fetchByTitle(title.trim());

        if (message != null) {
            redirectAttributes.addFlashAttribute("message", message);
        }

        return String.format("redirect:/animes/%d", animeServiceModel.getId());
    }

    @GetMapping("/search")
    @ResponseBody
    public List<AnimeViewModel> animesByTitle(@RequestParam String search) {
        List<AnimeServiceModel> foundAnimes = this.animeService.fetchAllStartsWithSearchValue(search);

        return this.collectionMapperUtil.map(foundAnimes, AnimeViewModel.class);
    }

    @ResponseBody
    @PostMapping("/like")
    public AnimeViewModel postLike(@RequestParam("title") String title, Principal principal) {
        try {
            AnimeServiceModel animeServiceModel = this.animeService.likeByTitle(title, principal.getName());
            return this.modelMapper.map(animeServiceModel, AnimeViewModel.class);
        } catch (IllegalStateException ise) {
            return new AnimeViewModel();
        }
    }

    @PostMapping("/myAnimeList/add")
    public String postAddToMyAnimeList(@RequestParam String title,
                                       Principal principal) {
        try {
            this.userService.addToMyAnimeList(title, principal.getName());
        } catch (IllegalStateException ise) {
            return String.format("redirect:/animes/byTitle?title=%s&message=%s", title, ise.getMessage());
        }

        return String.format("redirect:/animes/byTitle?title=%s", title);
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String postDelete(@RequestParam Long id) {
        AnimeServiceModel animeServiceModel = this.animeService.fetchById(id);

        this.userService.deleteAnimeFromAllUsersList(animeServiceModel);
        this.animeService.delete(id);

        return "redirect:/home";
    }

}

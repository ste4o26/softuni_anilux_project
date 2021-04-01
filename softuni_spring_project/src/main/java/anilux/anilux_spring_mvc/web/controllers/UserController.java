package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.binding_models.UserLoginBindingModel;
import anilux.anilux_spring_mvc.domain.binding_models.UserRegisterBindingModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.UserAdminViewModel;
import anilux.anilux_spring_mvc.domain.view_models.UserViewModel;
import anilux.anilux_spring_mvc.exceptions.UserAlreadyExistsException;
import anilux.anilux_spring_mvc.services.interfaces.RecommendationService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService, ModelMapper modelMapper) {
        this.userService = userService;
        this.recommendationService = recommendationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
            model.addAttribute("areDifferentPasswords", false);
        }

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("userRegisterBindingModel")
                                       UserRegisterBindingModel userRegisterBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("areDifferentPasswords", true);
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            return "redirect:/users/register";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            return "redirect:/users/register";
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);

        try {
            this.userService.register(userServiceModel);
        } catch (UserAlreadyExistsException uae) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("isExists", true);
            return "redirect:/users/register";
        }

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
        }

        return "login";
    }

    @PostMapping("/login-error")
    public String postLoginError(@Valid @ModelAttribute("userLoginBindingModel") UserLoginBindingModel userLoginBindingModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel", bindingResult);
            return "redirect:/users/login";
        }

        redirectAttributes.addFlashAttribute("wrongCredentials", true);
        redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);

        return "redirect:/users/login";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String all(Model model) {
        List<UserAdminViewModel> allUsers = this.userService.fetchAll();

        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", "");
        }

        model.addAttribute("allUsers", allUsers);

        return "all-users";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String postUpdateRoles(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "forPromotion") boolean forPromotion) {
        this.userService.updateRoles(username, forPromotion);

        return "redirect:/users/all";
    }

    @GetMapping("/myProfile")
    public String profile(Model model, Principal principal) {
        UserViewModel userViewModel = this.recommendationService.recommendAnimes(principal.getName());

        model.addAttribute("userViewModel", userViewModel);

        return "profile";
    }
}

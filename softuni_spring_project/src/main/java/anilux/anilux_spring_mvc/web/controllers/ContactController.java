package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.binding_models.EmailBindingModel;
import anilux.anilux_spring_mvc.domain.service_models.EmailServiceModel;
import anilux.anilux_spring_mvc.services.interfaces.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public ContactController(EmailService emailService, ModelMapper modelMapper) {
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/contactUs")
    public String contacts(Model model) {
        if (!model.containsAttribute("emailBindingModel")) {
            model.addAttribute("emailBindingModel", new EmailBindingModel());
        }

        return "contacts";
    }

    @PostMapping("/feedback")
    public String postFeedback(@ModelAttribute("emailBindingModel")
                                       EmailBindingModel emailBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailBindingModel", emailBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailBindingModel", bindingResult);
            return "redirect:/contacts/contactUs";
        }

        EmailServiceModel emailServiceModel = this.modelMapper.map(emailBindingModel, EmailServiceModel.class);
        emailServiceModel.setUsername(principal.getName());

        this.emailService.sendEmail(emailServiceModel);

        return "redirect:/contacts/contactUs";
    }

}

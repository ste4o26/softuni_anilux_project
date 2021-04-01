package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.domain.service_models.CommentServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.CommentViewModel;
import anilux.anilux_spring_mvc.services.interfaces.CommentService;
import anilux.anilux_spring_mvc.services.interfaces.EpisodeService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final EpisodeService episodeService;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper, UserService userService, EpisodeService episodeService) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.episodeService = episodeService;
    }

    @ResponseBody
    @PostMapping("/add")
    public CommentViewModel postAdd(@RequestBody String content, @RequestParam Long episodeId, Principal principal) {
        UserServiceModel userServiceModel = this.userService.fetchByUsername(principal.getName());
        EpisodeServiceModel episodeServiceModel = this.episodeService.fetchById(episodeId);

        CommentServiceModel commentServiceModel = new CommentServiceModel();
        commentServiceModel.setContent(content);
        commentServiceModel.setUser(userServiceModel);
        commentServiceModel.setEpisode(episodeServiceModel);

        return this.modelMapper.map(this.commentService.persist(commentServiceModel), CommentViewModel.class);
    }
}

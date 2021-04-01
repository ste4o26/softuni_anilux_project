package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.CommentServiceModel;

public interface CommentService {

    CommentServiceModel persist(CommentServiceModel commentServiceModel);

    CommentServiceModel fetchById(Long id);
}

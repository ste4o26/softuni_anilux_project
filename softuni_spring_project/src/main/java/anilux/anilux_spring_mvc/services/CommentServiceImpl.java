package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Comment;
import anilux.anilux_spring_mvc.domain.service_models.CommentServiceModel;
import anilux.anilux_spring_mvc.repositories.CommentRepository;
import anilux.anilux_spring_mvc.services.interfaces.CommentService;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public CommentServiceModel persist(CommentServiceModel commentServiceModel) {
        if (!this.validatorUtil.isValid(commentServiceModel)) {
            StringBuilder finalErrorMassage = new StringBuilder();
            Set<ConstraintViolation<CommentServiceModel>> violations =
                    this.validatorUtil.violations(commentServiceModel);

            violations.forEach(commentServiceModelViolation -> finalErrorMassage.append(commentServiceModelViolation.getMessage()));

            throw new IllegalArgumentException(finalErrorMassage.toString());
        }

        Comment comment = this.modelMapper.map(commentServiceModel, Comment.class);
        Comment result = this.commentRepository.saveAndFlush(comment);

        return this.modelMapper.map(result, CommentServiceModel.class);
    }

    @Override
    public CommentServiceModel fetchById(Long id) throws EntityNotFoundException {
        Comment comment = this.commentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id %d does not exists!", id)));

        return this.modelMapper.map(comment, CommentServiceModel.class);
    }
}

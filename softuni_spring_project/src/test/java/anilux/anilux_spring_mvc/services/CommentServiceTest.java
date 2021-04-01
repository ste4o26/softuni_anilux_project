package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Comment;
import anilux.anilux_spring_mvc.domain.entities.Episode;
import anilux.anilux_spring_mvc.domain.entities.User;
import anilux.anilux_spring_mvc.domain.service_models.CommentServiceModel;
import anilux.anilux_spring_mvc.repositories.CommentRepository;
import anilux.anilux_spring_mvc.services.interfaces.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest extends BaseTest {
    Comment comment;

    @MockBean
    CommentRepository commentRepository;

    @Autowired
    CommentService service;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("testUser");

        Episode episode = new Episode();
        episode.setNumber(1);

        comment = new Comment("Valid Test Comment", user, episode);
    }

    @AfterEach
    void reset() {
        Mockito.reset(commentRepository);
    }

    @Test
    public void persist_whenIsValidComment_shouldBePersistedSuccessfully() {
        Mockito.when(commentRepository.saveAndFlush(comment))
                .thenReturn(comment);

        CommentServiceModel commentServiceModel = modelMapper.map(comment, CommentServiceModel.class);

        service.persist(commentServiceModel);

        Mockito.verify(commentRepository, Mockito.times(1)).saveAndFlush(comment);
    }

    @Test
    public void persist_whenIsINVALIDComment_shouldThrowException() {
        CommentServiceModel commentServiceModel = new CommentServiceModel();
        assertThrows(IllegalArgumentException.class, () -> service.persist(commentServiceModel));
    }

    @Test
    public void fetchById_whenIsPresentCommentWithId_shouldReturnIt() {
        comment.setId(1L);

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        CommentServiceModel actual = service.fetchById(1L);

        assertEquals(1L, actual.getId());
        assertEquals(comment, modelMapper.map(actual, Comment.class));
    }

    @Test
    public void fetchById_whenIsNOTPresentCommentWithId_shouldThrowException() {
        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.fetchById(1L));
    }
}
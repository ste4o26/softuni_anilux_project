package anilux.anilux_spring_mvc.repositories;

import anilux.anilux_spring_mvc.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
package anilux.anilux_spring_mvc.repositories;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Optional<Anime> findByNameIgnoreCase(String name);

    List<Anime> findFirst10ByOrderByLikesDesc();

    List<Anime> findFirst10ByOrderByReleaseDateDesc();

    List<Anime> findByNameStartsWithIgnoreCase(String searchValue);

    @Query("SELECT a FROM animes AS a " +
            "JOIN a.genres AS a_g " +
            "WHERE a_g.name = ?1")
    Set<Anime> findAllWithGenre(String genre);
}

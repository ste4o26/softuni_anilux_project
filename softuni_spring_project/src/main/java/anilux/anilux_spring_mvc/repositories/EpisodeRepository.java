package anilux.anilux_spring_mvc.repositories;

import anilux.anilux_spring_mvc.domain.entities.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    Optional<Episode> findBySeasonIdAndNumber(Long seasonId, Integer number);
}

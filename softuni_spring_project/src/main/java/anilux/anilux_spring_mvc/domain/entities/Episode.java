package anilux.anilux_spring_mvc.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "episodes")
@Table(name = "episodes")
public class Episode extends BaseEntity {

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "episode_url", nullable = false)
    private String episodeUrl;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @ManyToOne(targetEntity = Season.class)
    @JoinColumn(name = "season_id", referencedColumnName = "id")
    private Season season;

    @OneToMany(targetEntity = Comment.class, mappedBy = "episode", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Comment> comments;
}

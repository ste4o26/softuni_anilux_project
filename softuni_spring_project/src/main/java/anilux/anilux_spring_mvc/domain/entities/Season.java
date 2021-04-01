package anilux.anilux_spring_mvc.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "seasons")
@Table(name = "seasons")
public class Season extends BaseEntity {
    @Column(name = "number", nullable = false)
    private Integer number;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = Anime.class)
    @JoinColumn(name = "anime_id", referencedColumnName = "id")
    private Anime anime;

    @ToString.Exclude
    @OneToMany(targetEntity = Episode.class, mappedBy = "season", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Episode> episodes;
}

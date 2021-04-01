package anilux.anilux_spring_mvc.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "genres")
@Table(name = "genres")
public class Genre extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(targetEntity = Anime.class, mappedBy = "genres", fetch = FetchType.EAGER)
    private Set<Anime> anime;
}

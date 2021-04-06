package anilux.anilux_spring_mvc.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity(name = "animes")
@Table(name = "animes")
public class Anime extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "image_thumbnail_url", nullable = false)
    private String imageThumbnailUrl;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "likes")
    private Integer likes;

    @ToString.Exclude
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> likedBy;

    @Column(name = "trailer_url", nullable = false)
    private String trailerUrl;

    @ToString.Exclude
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.EAGER)
    @JoinTable(name = "animes_genres",
            joinColumns = @JoinColumn(name = "anime_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres;

    @ToString.Exclude
    @OneToMany(targetEntity = Season.class, mappedBy = "anime", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Season> seasons;
}

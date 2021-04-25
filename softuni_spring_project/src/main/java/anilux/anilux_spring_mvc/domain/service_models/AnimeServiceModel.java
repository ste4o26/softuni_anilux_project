package anilux.anilux_spring_mvc.domain.service_models;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnimeServiceModel extends BaseServiceModel {

    @NotNull(message = "Title can't be null")
    @Size(min = 5, max = 50, message = "Title must be at least 5 characters long!")
    private String name;

    @NotNull(message = "Summary can't be null")
    @Size(min = 15, message = "Summary must be at least 15 characters long!")
    private String summary;

    @NotNull(message = "Image can't be null")
    private String imageThumbnailUrl;

    private LocalDate releaseDate;

    private boolean isCompleted;

    private Integer likes;

    @ToString.Exclude
    private Set<String> likedBy;

    //    TODO make it work
//    @Pattern(regexp = "")
    @NotNull(message = "Trailer can't be null")
    @Size(min = 10, message = "Trailer url must be at least 10 characters long!")
    private String trailerUrl;

    @NotNull(message = "Genres can't be null")
    @Size(min = 1, message = "At least one genre should be picked!")
    @ToString.Exclude
    private Set<GenreServiceModel> genres;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SeasonServiceModel> seasons;

    public void like() {
        int likes = this.getLikes() + 1;
        this.setLikes(likes);
    }
}

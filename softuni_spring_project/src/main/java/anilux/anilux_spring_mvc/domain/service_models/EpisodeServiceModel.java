package anilux.anilux_spring_mvc.domain.service_models;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EpisodeServiceModel extends BaseServiceModel {

    @NotNull(message = "Number can't be null!")
    @Min(1)
    private Integer number;

    @NotNull(message = "Episode URL can't be null!")
    private String episodeUrl;

    @NotNull(message = "Release date can't be null!")
    @PastOrPresent(message = "Release date can't be in the future!")
    private LocalDate releaseDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CommentServiceModel> comments;

    @NotNull(message = "Season can't be null!")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SeasonServiceModel season;
}

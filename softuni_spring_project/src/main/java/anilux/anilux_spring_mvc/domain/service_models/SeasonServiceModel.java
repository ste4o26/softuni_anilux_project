package anilux.anilux_spring_mvc.domain.service_models;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeasonServiceModel extends BaseServiceModel {
    @NotNull
    @Min(value = 1, message = "Episode number can't be less than 1")
    private Integer number;

    @NotNull
    @ToString.Exclude
    private AnimeServiceModel anime;

    @EqualsAndHashCode.Exclude
    private Set<EpisodeServiceModel> episodes;

}

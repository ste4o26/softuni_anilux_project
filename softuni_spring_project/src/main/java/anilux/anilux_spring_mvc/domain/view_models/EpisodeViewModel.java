package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EpisodeViewModel extends BaseViewModel {
    private Integer number;

    private String episodeUrl;

    private Set<CommentViewModel> comments;

    @EqualsAndHashCode.Exclude
    private SeasonViewModel season;
}

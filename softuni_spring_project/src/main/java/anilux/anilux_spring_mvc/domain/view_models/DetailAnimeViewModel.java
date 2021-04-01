package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetailAnimeViewModel extends BaseViewModel {
    private String name;
    private String summary;
    private String trailerUrl;
    private String imageThumbnailUrl;
    private LocalDate releaseDate;
    private boolean isCompleted;
    private Set<GenreViewModel> genres;

    @EqualsAndHashCode.Exclude
    private Set<SeasonViewModel> seasons;

    public void sortSeasonsBySeasonNumber() {
        this.setSeasons(this.getSeasons()
                .stream()
                .sorted(Comparator.comparing(SeasonViewModel::getNumber))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
}

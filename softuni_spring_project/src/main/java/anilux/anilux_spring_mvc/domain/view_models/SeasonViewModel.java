package anilux.anilux_spring_mvc.domain.view_models;

import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeasonViewModel extends BaseViewModel {
    //TODO
    private Integer number;

    private DetailAnimeViewModel anime;

    private List<EpisodeViewModel> episodes;

    public void sortEpisodes(Set<EpisodeServiceModel> episodes, ModelMapper modelMapper) {
        List<EpisodeViewModel> sortedEpisodes = episodes
                .stream()
                .map(e -> modelMapper.map(e, EpisodeViewModel.class))
                .sorted((e1, e2) -> e1.getNumber() - e2.getNumber())
                .collect(Collectors.toList());

        this.setEpisodes(sortedEpisodes);
    }
}

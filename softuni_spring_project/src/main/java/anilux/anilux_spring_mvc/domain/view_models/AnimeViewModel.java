package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnimeViewModel extends BaseViewModel{
    private String imageThumbnailUrl;
    private String name;
    private Integer likes;
}

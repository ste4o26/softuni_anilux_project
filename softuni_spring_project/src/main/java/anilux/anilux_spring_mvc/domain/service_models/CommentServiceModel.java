package anilux.anilux_spring_mvc.domain.service_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentServiceModel extends BaseServiceModel {

    @NotNull(message = "Comment content can't be null!")
    @Size(min = 10, message = "Comment must be at least 10 symbols")
    private String content;

    @NotNull(message = "User can't be null!")
    private UserServiceModel user;

    @NotNull(message = "Episode can't be null!")
    @EqualsAndHashCode.Exclude
    private EpisodeServiceModel episode;
}

package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentViewModel extends BaseViewModel {
    private String content;

    private String userUsername;
}

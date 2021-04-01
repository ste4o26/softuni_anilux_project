package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserViewModel {
    private String username;

    private String email;

    private List<AnimeViewModel> recommended;
}

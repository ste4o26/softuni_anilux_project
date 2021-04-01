package anilux.anilux_spring_mvc.domain.service_models;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenreServiceModel extends BaseServiceModel {

    @NotNull(message = "Genre name can't be null!")
    @Size(min = 3, max = 20, message = "Genre name must be between 3 and 20 characters!")
    private String name;
}

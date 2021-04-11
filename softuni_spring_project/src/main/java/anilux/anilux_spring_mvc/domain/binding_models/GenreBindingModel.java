package anilux.anilux_spring_mvc.domain.binding_models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenreBindingModel {
    private Long id;

    @NotNull
    @Size(min = 3, max = 20, message = "Genre must be between 3 and 20 characters!")
    private String name;
}

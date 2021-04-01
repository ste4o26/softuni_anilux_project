package anilux.anilux_spring_mvc.domain.binding_models;

import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnimeBindingModel {

    @NotNull
    @Size(min = 5, max = 50, message = "Title must be at least 5 characters long!")
    private String name;

    @NotNull
    @Size(min = 15, message = "Summary must be at least 15 characters long!")
    private String summary;

    @NotNull
    private MultipartFile image;

    //TODO    @Pattern(regexp = "")
    @NotNull
    @Size(min = 10, message = "Trailer url must be at least 10 characters long!")
    private String trailerUrl;

    @NotNull
    @Size(min = 1, message = "At least one genre should be picked!")
    private List<GenreServiceModel> genres;
}

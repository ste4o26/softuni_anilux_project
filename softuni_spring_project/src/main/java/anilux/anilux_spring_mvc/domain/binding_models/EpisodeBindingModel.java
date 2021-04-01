package anilux.anilux_spring_mvc.domain.binding_models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EpisodeBindingModel {

    @NotNull
    private MultipartFile episode;

    private Long seasonId;
}

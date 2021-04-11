package anilux.anilux_spring_mvc.domain.binding_models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailBindingModel {

    @NotNull
    @Size(min = 15, message = "Feedback must be at least 15 characters!")
    private String content;
}

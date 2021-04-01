package anilux.anilux_spring_mvc.domain.binding_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginBindingModel {

    @NotNull(message = "Username can't be null!")
    @Size(min = 5, message = "Username must be at least 5 characters")
    private String username;

    @NotNull(message = "Password cant be null!")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
            message = "Password should contain one upper case letter one lower case letter and one digit at least!")
    private String password;
}

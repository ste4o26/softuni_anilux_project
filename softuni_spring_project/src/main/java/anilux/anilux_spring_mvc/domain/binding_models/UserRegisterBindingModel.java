package anilux.anilux_spring_mvc.domain.binding_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterBindingModel {
    @Size(min = 5, message = "Username must be at least 5 characters!")
    private String username;

    @Email(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,3}$", message = "Email must contain (@ and .)!")
    private String email;

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
            message = "Password should contain one upper case letter one lower case letter and one digit at least!")
    private String password;

    @Size(min = 6, max = 20, message = "Password confirmation must be between 6 and 20 characters!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
            message = "Password should contain one upper case letter one lower case letter and one digit at least!")
    private String confirmPassword;
}

package anilux.anilux_spring_mvc.domain.service_models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserServiceModel extends BaseServiceModel {

    @NotNull(message = "Username can't be null!")
    @Size(min = 5, max = 50)
    private String username;

    @NotNull(message = "Password can't be null!")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
            message = "Password should contain one upper case letter one lower case letter and one digit at least!")
    private String password;

    @NotNull(message = "Email can't be null!")
    @Email(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,3}$", message = "Email must contain (@ and .)!")
    private String email;

    @NotNull(message = "Authorities can't be null!")
    private Set<RoleServiceModel> authorities;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AnimeServiceModel> myList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CommentServiceModel> comments;
}

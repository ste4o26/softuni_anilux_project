package anilux.anilux_spring_mvc.domain.view_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAdminViewModel {
    private String username;

    private String email;

    private Set<RoleViewModel> authorities;
}

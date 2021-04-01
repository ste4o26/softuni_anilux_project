package anilux.anilux_spring_mvc.domain.service_models;

import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleServiceModel extends BaseServiceModel {

    @NotNull
    private RoleName authority;
}

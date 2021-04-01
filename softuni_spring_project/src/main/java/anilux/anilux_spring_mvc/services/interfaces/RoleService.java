package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.service_models.RoleServiceModel;

import java.util.List;

public interface RoleService {
    void persist(RoleServiceModel roleServiceModel);

    boolean isEmpty();

    int findBiggestAuthorityLevel(List<RoleName> authorities);

    RoleServiceModel fetchByRoleName(RoleName authority);

    List<RoleServiceModel> fetchAll();
}

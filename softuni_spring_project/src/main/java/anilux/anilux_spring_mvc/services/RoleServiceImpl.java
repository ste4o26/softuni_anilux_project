package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Role;
import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.service_models.RoleServiceModel;
import anilux.anilux_spring_mvc.exceptions.RoleAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.RoleRepository;
import anilux.anilux_spring_mvc.services.interfaces.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void persist(RoleServiceModel roleServiceModel) {
        try {
            this.fetchByRoleName(roleServiceModel.getAuthority());
            throw new RoleAlreadyExistsException(String.format("Role %s already exists!", roleServiceModel.getAuthority().name()));

        } catch (EntityNotFoundException ignore) {
            Role role = this.modelMapper.map(roleServiceModel, Role.class);
            this.roleRepository.saveAndFlush(role);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.roleRepository.count() == 0;
    }

    @Override
    public int findBiggestAuthorityLevel(List<RoleName> authorities) {
        int biggestAuthorityLevel = 2;
        for (RoleName authority : authorities) {
            if (authority.getAuthorityLevel() < biggestAuthorityLevel) {
                biggestAuthorityLevel = authority.getAuthorityLevel();
            }
        }

        return biggestAuthorityLevel;
    }

    @Override
    public RoleServiceModel fetchByRoleName(RoleName authority) {
        Role role = this.roleRepository
                .findByAuthority(authority)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Role %s does not exists!", authority)));

        return this.modelMapper.map(role, RoleServiceModel.class);
    }

    @Override
    public List<RoleServiceModel> fetchAll() {
        return this.roleRepository
                .findAll()
                .stream()
                .map(role -> this.modelMapper.map(role, RoleServiceModel.class))
                .collect(Collectors.toList());
    }

}

package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Role;
import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.service_models.RoleServiceModel;
import anilux.anilux_spring_mvc.exceptions.RoleAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.RoleRepository;
import anilux.anilux_spring_mvc.services.interfaces.RoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static anilux.anilux_spring_mvc.domain.entities.enums.RoleName.*;
import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest extends BaseTest {
    Role role;

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleService service;

    @BeforeEach
    void setup() {
        role = new Role(ROLE_ROOT_ADMIN);
    }

    @AfterEach
    void reset() {
        Mockito.reset(roleRepository);
    }

    @Test
    public void persist_whenIsNotExistingRole_shouldPersisIt() {
        Mockito.reset(roleRepository);

        Mockito.when(roleRepository.findByAuthority(ROLE_ROOT_ADMIN))
                .thenThrow(EntityNotFoundException.class);

        RoleServiceModel roleServiceModel = modelMapper.map(role, RoleServiceModel.class);

        service.persist(roleServiceModel);

        Mockito.verify(roleRepository, Mockito.times(1)).saveAndFlush(role);
    }

    @Test
    public void persist_whenIsExistingRole_shouldThrowException() {
        Mockito.when(roleRepository.findByAuthority(ROLE_ROOT_ADMIN))
                .thenReturn(Optional.of(role));

        RoleServiceModel roleServiceModel = modelMapper.map(role, RoleServiceModel.class);

        assertThrows(RoleAlreadyExistsException.class, () -> service.persist(roleServiceModel));
    }

    @Test
    public void findBiggestAuthorityLevel_whenHighestRoleIsRoleRootAdmin_shouldReturn0() {
        List<RoleName> authorities = List.of(ROLE_ROOT_ADMIN, ROLE_ADMIN, ROLE_USER);

        int biggestAuthorityLevel = service.findBiggestAuthorityLevel(authorities);

        assertEquals(0, biggestAuthorityLevel);
    }

    @Test
    public void findBiggestAuthorityLevel_whenHighestRoleIsRoleAdmin_shouldReturn1() {
        List<RoleName> authorities = List.of(ROLE_ADMIN, ROLE_USER);

        int biggestAuthorityLevel = service.findBiggestAuthorityLevel(authorities);

        assertEquals(1, biggestAuthorityLevel);
    }

    @Test
    public void findBiggestAuthorityLevel_whenHighestRoleIsRoleUser_shouldReturn2() {
        List<RoleName> authorities = List.of(ROLE_USER);

        int biggestAuthorityLevel = service.findBiggestAuthorityLevel(authorities);

        assertEquals(2, biggestAuthorityLevel);
    }

    @Test
    public void fetchByRoleName_whenIsExistingRole_shouldReturnIt() {
        Mockito.when(roleRepository.findByAuthority(ROLE_ROOT_ADMIN))
                .thenReturn(Optional.of(role));

        RoleServiceModel actual = service.fetchByRoleName(ROLE_ROOT_ADMIN);

        assertEquals(ROLE_ROOT_ADMIN, actual.getAuthority());
    }

    @Test
    public void fetchByRoleName_whenIsNOTExistingRole_shouldThrowException() {
        Mockito.when(roleRepository.findByAuthority(ROLE_ADMIN))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.fetchByRoleName(ROLE_ROOT_ADMIN));
    }

    @Test
    public void fetchAll_whenPresentRolesInDB_shouldReturnThemAll() {
        List<Role> authorities =
                List.of(new Role(ROLE_ROOT_ADMIN), new Role(ROLE_ADMIN), new Role(ROLE_USER));

        Mockito.when(roleRepository.findAll())
                .thenReturn(authorities);

        List<RoleServiceModel> actual = service.fetchAll();

        assertEquals(3, actual.size());
        assertTrue(actual.contains(modelMapper.map(role, RoleServiceModel.class)));
    }

    @Test
    public void fetchAll_whenEmptyDB_shouldReturnEmptyList() {
        List<Role> authorities = new ArrayList<>();

        Mockito.when(roleRepository.findAll())
                .thenReturn(authorities);

        List<RoleServiceModel> actual = service.fetchAll();

        assertEquals(0, actual.size());
    }
}
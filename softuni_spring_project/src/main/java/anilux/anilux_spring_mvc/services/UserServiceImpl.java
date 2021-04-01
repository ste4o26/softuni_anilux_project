package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Role;
import anilux.anilux_spring_mvc.domain.entities.User;
import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.RoleServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.UserAdminViewModel;
import anilux.anilux_spring_mvc.exceptions.UserAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.UserRepository;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.RoleService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailServiceImpl;
    private final AnimeService animeService;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           RoleService roleService,
                           UserDetailsServiceImpl userDetailServiceImpl,
                           AnimeService animeService,
                           PasswordEncoder passwordEncoder,
                           ValidatorUtil validatorUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.animeService = animeService;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
    }

    private List<RoleName> mapAuthoritiesToRolNames(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(authority -> this.roleService.fetchByRoleName(RoleName.valueOf(authority.getAuthority())))
                .map(authority -> this.modelMapper.map(authority, Role.class))
                .map(Role::getAuthority)
                .collect(Collectors.toList());
    }

    private Set<RoleServiceModel> getRolesForUser() {
        if (this.userRepository.count() == 0) {
            return new HashSet<>(this.roleService.fetchAll());
        } else {
            RoleServiceModel roleServiceModel = this.roleService.fetchByRoleName(RoleName.ROLE_USER);
            HashSet<RoleServiceModel> roles = new HashSet<>();
            roles.add(roleServiceModel);
            return roles;
        }
    }

    private boolean isExistingUser(String username) {
        try {
            this.fetchByUsername(username);
            return true;
        } catch (UsernameNotFoundException unfe) {
            return false;
        }
    }

    @Override
    public UserServiceModel updateRoles(String userToBeUpdateUsername, boolean forPromotion)
            throws UsernameNotFoundException, IllegalStateException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<RoleName> currentUserAuthorities = this.mapAuthoritiesToRolNames(authentication.getAuthorities());

        List<RoleName> updatedUserAuthorities =
                this.mapAuthoritiesToRolNames(this.userDetailServiceImpl.loadUserByUsername(userToBeUpdateUsername).getAuthorities());

        int currentUserBiggestAuthorityLevel = this.roleService.findBiggestAuthorityLevel(currentUserAuthorities);
        int updatedUserBiggestAuthorityLevel = this.roleService.findBiggestAuthorityLevel(updatedUserAuthorities);

        User updatedUser = this.modelMapper.map(this.fetchByUsername(userToBeUpdateUsername), User.class);

        if (updatedUserBiggestAuthorityLevel == 2 && !forPromotion) {
            throw new IllegalStateException("You can not demote user who has only the lowest authority!");
        }

        if (currentUserBiggestAuthorityLevel < updatedUserBiggestAuthorityLevel - 1 && forPromotion) {
            RoleName newAuthorityName = RoleName.values()[updatedUserBiggestAuthorityLevel - 1];
            Role authority = this.modelMapper.map(this.roleService.fetchByRoleName(newAuthorityName), Role.class);
            updatedUser.getAuthorities().add(authority);

        } else if (currentUserBiggestAuthorityLevel < updatedUserBiggestAuthorityLevel && !forPromotion) {
            HashSet<Role> authorities = new HashSet<>();
            authorities.add(this.modelMapper.map(this.roleService.fetchByRoleName(RoleName.ROLE_USER), Role.class));
            updatedUser.setAuthorities(authorities);

        } else {
            throw new IllegalStateException("You can not promote / demote user with same or higher authorities!");
        }

        this.userRepository.saveAndFlush(updatedUser);
        return this.modelMapper.map(updatedUser, UserServiceModel.class);
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) throws UserAlreadyExistsException {
        Set<RoleServiceModel> roles = getRolesForUser();
        userServiceModel.setAuthorities(roles);

        if (!this.validatorUtil.isValid(userServiceModel)) {
            Set<ConstraintViolation<UserServiceModel>> violations = this.validatorUtil.violations(userServiceModel);

            StringBuilder finalErrorMassage = new StringBuilder();
            violations.forEach(userServiceModelViolation -> finalErrorMassage.append(userServiceModelViolation.getMessage()));

            throw new IllegalArgumentException(finalErrorMassage.toString());
        }

        if (this.isExistingUser(userServiceModel.getUsername())) {
            throw new UserAlreadyExistsException(String.format("User %s already exists!", userServiceModel.getUsername()));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.passwordEncoder.encode(userServiceModel.getPassword()));
        user.setComments(new HashSet<>());
        user.setMyList(new HashSet<>());

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<User> deleteAnimeFromAllUsersList(AnimeServiceModel animeServiceModel) {
        //will throw an exception if the anime which ahs to be deleted does not exists!
        this.animeService.fetchByTitle(animeServiceModel.getName());

        List<User> allUsers = this.userRepository.findAll();

        for (User user : allUsers) {
            Set<Anime> filteredAnimes = user.getMyList()
                    .stream()
                    .filter(anime -> !anime.getName().equals(animeServiceModel.getName()))
                    .collect(Collectors.toSet());

            user.setMyList(filteredAnimes);
            this.userRepository.saveAndFlush(user);
        }
        return allUsers;
    }

    @Override
    public void addToMyAnimeList(String title, String username) {
        AnimeServiceModel animeServiceModel = this.animeService.fetchByTitle(title);

        UserServiceModel userServiceModel = this.fetchByUsername(username);
        if (userServiceModel.getMyList() == null) {
            userServiceModel.setMyList(new LinkedHashSet<>());
        }

        if (userServiceModel.getMyList().contains(animeServiceModel)) {
            throw new IllegalStateException(String.format("%s has already been added to your list!", title));
        }

        userServiceModel.getMyList().add(animeServiceModel);

        this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel, User.class));
    }

    @Override
    public UserServiceModel fetchByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s do not exists!", username)));

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserAdminViewModel> fetchAll() {
        return this.userRepository
                .findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserAdminViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnimeServiceModel> fetchUserList(String username) {
        UserServiceModel userServiceModel = this.fetchByUsername(username);

        return new ArrayList<>(userServiceModel
                .getMyList());
    }
}

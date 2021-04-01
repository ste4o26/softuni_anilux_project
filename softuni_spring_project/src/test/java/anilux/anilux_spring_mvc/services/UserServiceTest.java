package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Role;
import anilux.anilux_spring_mvc.domain.entities.User;
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
import anilux.anilux_spring_mvc.utils.ValidatorUtilImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import javax.validation.Validation;
import java.time.LocalDate;
import java.util.*;

import static anilux.anilux_spring_mvc.domain.entities.enums.RoleName.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    private final String TEST_USERNAME = "testUser";
    private final String TEST_PASSWORD = "Test12345";
    private final String TEST_EMAIL = "test4e@gmail.com";

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;


    @Mock
    AnimeService animeService;

    UserServiceModel userServiceModel;
    AnimeServiceModel animeServiceModel;

    UserDetailsServiceImpl userDetailsServiceImpl;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;
    ValidatorUtil validatorUti;

    UserService service;

    @BeforeEach
    public void setUp() {
        userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USERNAME);
        userServiceModel.setPassword(TEST_PASSWORD);
        userServiceModel.setEmail(TEST_EMAIL);
        userServiceModel.setMyList(new HashSet<>());

        animeServiceModel = new AnimeServiceModel();
        animeServiceModel.setName("Naruto");
        animeServiceModel.setSummary("Some summery just in case...");
        animeServiceModel.setImageThumbnailUrl("/img/2.jpg");
        animeServiceModel.setReleaseDate(LocalDate.of(2016, 3, 27));
        animeServiceModel.setCompleted(false);
        animeServiceModel.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");

        modelMapper = new ModelMapper();
        validatorUti = new ValidatorUtilImpl(Validation.buildDefaultValidatorFactory().getValidator());
        passwordEncoder = new BCryptPasswordEncoder();
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);

        service = new UserServiceImpl(userRepository,
                modelMapper,
                roleService,
                userDetailsServiceImpl,
                animeService,
                passwordEncoder,
                validatorUti);

        Mockito.lenient().when(roleService.fetchByRoleName(ROLE_ADMIN))
                .thenReturn(new RoleServiceModel(ROLE_ADMIN));

        Mockito.lenient().when(roleService.fetchByRoleName(ROLE_ROOT_ADMIN))
                .thenReturn(new RoleServiceModel(ROLE_ROOT_ADMIN));

        Mockito.lenient().when(roleService.fetchByRoleName(ROLE_USER))
                .thenReturn(new RoleServiceModel(ROLE_USER));

        Mockito.lenient().when(roleService.findBiggestAuthorityLevel(List.of(ROLE_ROOT_ADMIN, ROLE_ADMIN, ROLE_USER)))
                .thenReturn(0);

        Mockito.lenient().when(roleService.findBiggestAuthorityLevel(List.of(ROLE_ADMIN, ROLE_USER)))
                .thenReturn(1);

        Mockito.lenient().when(roleService.findBiggestAuthorityLevel(List.of(ROLE_USER)))
                .thenReturn(2);
    }

    @Test
    public void register_whenCorrectCredentials_shouldRegisterUser() {
        Mockito.when(roleService.fetchAll())
                .thenReturn(List.of(new RoleServiceModel(ROLE_ROOT_ADMIN),
                        new RoleServiceModel(ROLE_ADMIN),
                        new RoleServiceModel(ROLE_USER)));

        User user = modelMapper.map(userServiceModel, User.class);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);

        UserServiceModel actualResult = service.register(userServiceModel);

        assertEquals(userServiceModel.getUsername(), actualResult.getUsername());
    }

    @Test
    public void register_whenNOTCorrectCredentials_shouldThrowException() {
        //given
        UserServiceModel userServiceModel1 = new UserServiceModel();
        userServiceModel1.setUsername("bad");
        userServiceModel1.setPassword("bad");
        userServiceModel1.setEmail("bad");

        UserServiceModel userServiceModel2 = new UserServiceModel();
        userServiceModel2.setUsername("ValidUsername");
        userServiceModel2.setPassword("wrong");
        userServiceModel2.setEmail("valid@gmail.com");

        UserServiceModel userServiceModel3 = new UserServiceModel();
        userServiceModel3.setUsername("ValidUsername");
        userServiceModel3.setPassword("Valid12345");
        userServiceModel3.setEmail("invalid");

        UserServiceModel userServiceModel4 = new UserServiceModel();
        userServiceModel4.setUsername("bad");
        userServiceModel4.setPassword("Valid12345");
        userServiceModel4.setEmail("valid@gmail.com");

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> service.register(userServiceModel1));
        assertThrows(IllegalArgumentException.class, () -> service.register(userServiceModel2));
        assertThrows(IllegalArgumentException.class, () -> service.register(userServiceModel3));
        assertThrows(IllegalArgumentException.class, () -> service.register(userServiceModel4));
    }

    @Test
    public void register_whenAlreadyExistingUserName_shouldThrowException() {
        Mockito.lenient().when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(modelMapper.map(userServiceModel, User.class)));

        assertThrows(UserAlreadyExistsException.class, () -> service.register(userServiceModel));
    }

    @Test
    public void register_whenIsNotFirstRegisteredUser_shouldHaveOnlyRoleUser() {
        Mockito.when(roleService.fetchByRoleName(ROLE_USER))
                .thenReturn(new RoleServiceModel(ROLE_USER));

        Mockito.when(userRepository.count())
                .thenReturn(1L);

        User user = modelMapper.map(userServiceModel, User.class);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);

        UserServiceModel actualResult = service.register(userServiceModel);

        assertEquals(userServiceModel.getUsername(), actualResult.getUsername());
    }

    @Test
    @WithMockUser(username = "rootAdmin", roles = {"ROOT_ADMIN", "ADMIN", "USER"})
    public void updateRoles_whenUserWithRoleRootAdminPromoteUserWithRoleUser_shouldBePromotedSuccessfully() {
        User promotedUser = modelMapper.map(userServiceModel, User.class);
        promotedUser.setAuthorities(Set.of(new Role(ROLE_USER)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(promotedUser));

        Mockito.when(userRepository.saveAndFlush(promotedUser))
                .thenReturn(promotedUser);

        UserServiceModel userServiceModel = service.updateRoles(TEST_USERNAME, true);

        assertEquals(2, userServiceModel.getAuthorities().size());
        assertEquals(TEST_USERNAME, userServiceModel.getUsername());

        RoleServiceModel roleAdmin = new RoleServiceModel(ROLE_ADMIN);
        assertTrue(userServiceModel.getAuthorities().contains(roleAdmin));
    }

    @Test
    @WithMockUser(username = "rootAdmin", roles = {"ROOT_ADMIN", "ADMIN", "USER"})
    public void updateRoles_whenUserWithRoleRootAdminPromoteUserWithRoleAdmin_shouldBeThrownException() {
        User promotedUser = modelMapper.map(userServiceModel, User.class);
        promotedUser.setAuthorities(Set.of(new Role(ROLE_USER), new Role(ROLE_ADMIN)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(promotedUser));

        assertThrows(IllegalStateException.class, () -> service.updateRoles(TEST_USERNAME, true));
    }

    @Test
    @WithMockUser(username = "rootAdmin", roles = {"ROOT_ADMIN", "ADMIN", "USER"})
    public void updateRoles_whenUserWithRoleRootAdminDemoteUserWithRoleAdmin_shouldBeDemotedSuccessfully() {
        User demotedUser = modelMapper.map(userServiceModel, User.class);
        demotedUser.setAuthorities(Set.of(new Role(ROLE_USER), new Role(ROLE_ADMIN)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(demotedUser));

        Mockito.when(userRepository.saveAndFlush(demotedUser))
                .thenReturn(demotedUser);

        UserServiceModel userServiceModel = service.updateRoles(TEST_USERNAME, false);

        assertEquals(1, userServiceModel.getAuthorities().size());
        assertEquals(TEST_USERNAME, userServiceModel.getUsername());

        RoleServiceModel roleAdmin = new RoleServiceModel(ROLE_ADMIN);
        assertFalse(userServiceModel.getAuthorities().contains(roleAdmin));
    }


    @Test
    @WithMockUser(username = "rootAdmin", roles = {"ROOT_ADMIN", "ADMIN", "USER"})
    public void updateRoles_whenUserWithRoleRootAdminDemoteUserWithRoleUser_shouldBeThrownException() {
        User demotedUser = modelMapper.map(userServiceModel, User.class);
        demotedUser.setAuthorities(Set.of(new Role(ROLE_USER)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(demotedUser));

        assertThrows(IllegalStateException.class, () -> service.updateRoles(TEST_USERNAME, false));
    }


    private List<User> setUpUsersWithAnimeLists() {
        Set<Anime> testAnimeList = new HashSet<>();

        Anime anime1 = new Anime();
        anime1.setName("Haikyuu!");
        anime1.setSummary("Some summery just in case...");
        anime1.setImageThumbnailUrl("/img/1.jpg");
        anime1.setReleaseDate(LocalDate.of(2015, 2, 26));
        anime1.setCompleted(false);
        anime1.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        testAnimeList.add(anime1);

        Anime anime2 = new Anime();
        anime2.setName("Naruto");
        anime2.setSummary("Some summery just in case...");
        anime2.setImageThumbnailUrl("/img/2.jpg");
        anime2.setReleaseDate(LocalDate.of(2016, 3, 27));
        anime2.setCompleted(false);
        anime2.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        testAnimeList.add(anime2);

        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setUsername(TEST_USERNAME);
        user1.setPassword(TEST_PASSWORD);
        user1.setEmail(TEST_EMAIL);
        user1.setMyList(testAnimeList);
        users.add(user1);


        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(TEST_PASSWORD);
        user2.setEmail("user2@gmail.com");
        user2.setMyList(testAnimeList);
        users.add(user2);

        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword(TEST_PASSWORD);
        user3.setEmail("user3@gmail.com");
        user3.setMyList(testAnimeList);
        users.add(user3);

        return users;
    }

    @Test
    public void deleteAnimeFromAllUsersList_whenAnimeIsDeleted_shouldBeRemovedFromAllUsersList() {
        List<User> users = setUpUsersWithAnimeLists();

        Mockito.when(userRepository.findAll())
                .thenReturn(users);

        Mockito.when(userRepository.saveAndFlush(users.get(0)))
                .thenReturn(null);

        Mockito.when(userRepository.saveAndFlush(users.get(1)))
                .thenReturn(null);

        Mockito.when(userRepository.saveAndFlush(users.get(2)))
                .thenReturn(null);

        List<User> actualResult = service.deleteAnimeFromAllUsersList(animeServiceModel);

        assertEquals(1, actualResult.get(0).getMyList().size());
        assertEquals(1, actualResult.get(1).getMyList().size());
        assertEquals(1, actualResult.get(2).getMyList().size());

        assertFalse(actualResult.get(0).getMyList().contains(modelMapper.map(animeServiceModel, Anime.class)));
        assertFalse(actualResult.get(1).getMyList().contains(modelMapper.map(animeServiceModel, Anime.class)));
        assertFalse(actualResult.get(2).getMyList().contains(modelMapper.map(animeServiceModel, Anime.class)));
    }

    @Test
    void deleteAnimeFromAllUsersList_whenNotExistingAnime_shouldThrowAnException() {
        Mockito.when(animeService.fetchByTitle("Naruto"))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.deleteAnimeFromAllUsersList(animeServiceModel));
    }

    @Test
    public void addToMyAnimeList_whenNewAnimeIsPassed_shouldAddItSuccessfully() {
        User user = modelMapper.map(userServiceModel, User.class);

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(user));

        Mockito.when(animeService.fetchByTitle("Naruto"))
                .thenReturn(animeServiceModel);

        service.addToMyAnimeList("Naruto", TEST_USERNAME);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).saveAndFlush(argument.capture());

        User actual = argument.getValue();

        assertTrue(actual.getMyList().contains(modelMapper.map(animeServiceModel, Anime.class)));
        assertEquals(1, actual.getMyList().size());
    }

    @Test
    public void addToMyAnimeList_whenAnimeIsAlreadyInTheUserList_shouldThrowException() {
        User user = modelMapper.map(userServiceModel, User.class);
        user.setMyList(Set.of(modelMapper.map(animeServiceModel, Anime.class)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(user));

        Mockito.when(animeService.fetchByTitle("Naruto"))
                .thenReturn(animeServiceModel);

        assertThrows(IllegalStateException.class, () -> service.addToMyAnimeList("Naruto", TEST_USERNAME));
    }

    @Test
    public void fetchAll_whenUsersPresents_shouldReturnThemAll() {
        List<User> users = this.setUpUsersWithAnimeLists();

        Mockito.when(userRepository.findAll())
                .thenReturn(users);

        List<UserAdminViewModel> actual = service.fetchAll();

        assertEquals(3, actual.size());
        assertTrue(actual.contains(modelMapper.map(userServiceModel, UserAdminViewModel.class)));
    }

    @Test
    public void fetchAll_whenUsersNOTPresents_shouldReturnEmptyList() {
        Mockito.when(userRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<UserAdminViewModel> actual = service.fetchAll();

        assertEquals(0, actual.size());
    }

    @Test
    public void fetchUserList_whenUserWithAnimesInItsList_shouldReturnThemSuccessfully() {
        User user = modelMapper.map(userServiceModel, User.class);
        user.setMyList(Set.of(modelMapper.map(animeServiceModel, Anime.class)));

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(user));

        List<AnimeServiceModel> actual = service.fetchUserList(TEST_USERNAME);

        assertEquals(1, actual.size());
        assertTrue(actual.contains(animeServiceModel));
    }

    @Test
    public void fetchUserList_whenUserWITHOUTAnimesInItsList_shouldReturnEmptyList() {
        User user = modelMapper.map(userServiceModel, User.class);

        Mockito.when(userRepository.findByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(user));

        List<AnimeServiceModel> actual = service.fetchUserList(TEST_USERNAME);

        assertEquals(0, actual.size());
    }
}
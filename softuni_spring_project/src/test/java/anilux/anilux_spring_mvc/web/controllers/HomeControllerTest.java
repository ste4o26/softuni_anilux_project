package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.entities.Role;
import anilux.anilux_spring_mvc.domain.entities.User;
import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.view_models.AnimeViewModel;
import anilux.anilux_spring_mvc.repositories.AnimeRepository;
import anilux.anilux_spring_mvc.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;


@AutoConfigureMockMvc
class HomeControllerTest extends BaseTest {
    List<Anime> animes;

    User user;

    @MockBean
    AnimeRepository animeRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setup() {
        animes = new ArrayList<>();

        Genre genre = new Genre();
        genre.setName("Shounen");

        Anime anime1 = new Anime();
        anime1.setId(1L);
        anime1.setName("Black Clover");
        anime1.setSummary("Some summery just in case...");
        anime1.setImageThumbnailUrl("/img/2.jpg");
        anime1.setReleaseDate(LocalDate.of(2021, 3, 20));
        anime1.setCompleted(false);
        anime1.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        anime1.setLikes(18);
        anime1.setGenres(Set.of(genre));
        animes.add(anime1);

        Anime anime2 = new Anime();
        anime2.setId(2L);
        anime2.setName("Naruto");
        anime2.setSummary("Some summery just in case...");
        anime2.setImageThumbnailUrl("/img/2.jpg");
        anime2.setReleaseDate(LocalDate.of(2020, 8, 15));
        anime2.setCompleted(false);
        anime2.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        anime2.setLikes(3);
        anime2.setGenres(Set.of(genre));
        animes.add(anime2);

        user = new User();
        user.setMyList(new LinkedHashSet<>());
        user.setUsername("spring");
        user.setAuthorities(Set.of(new Role(RoleName.ROLE_USER)));

        Mockito.when(animeRepository.findFirst10ByOrderByReleaseDateDesc())
                .thenReturn(animes);

        Mockito.when(animeRepository.findFirst10ByOrderByLikesDesc())
                .thenReturn(animes);

        Mockito.when(animeRepository.findAll())
                .thenReturn(animes);

        Mockito.when(animeRepository.count())
                .thenReturn(2L);

        Mockito.when(userRepository.findByUsername("spring"))
                .thenReturn(Optional.of(user));

    }

    @Test
    public void index_whenNoUserIsAuthenticated_shouldReturnItSuccessfully() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @WithMockUser(value = "spring")
    @Test
    public void index_whenIsAuthenticatedAnyUser_shouldReturnErrorPage() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/");

        String expected = "/users/login?favicon=https%3A%2F%2Fres.cloudinary.com%2Fste4o26%2Fimage%2Fupload%2Fv1616251469%2Ffavicon-32x32_s4mmvh.png";

        mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.redirectedUrl(expected));
    }

    @WithMockUser("spring")
    @Test
    public void home_whenIsAuthenticatedAnyUser_shouldReturnItSuccessfully() throws Exception {
        List<AnimeViewModel> expected = collectionMapperUtil.map(animes, AnimeViewModel.class);
        List<AnimeViewModel> myListExpected = collectionMapperUtil.map(user.getMyList(), AnimeViewModel.class);

        mvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.model().attribute("mostPopular", expected))
                .andExpect(MockMvcResultMatchers.model().attribute("newlyReleased", expected))
                .andExpect(MockMvcResultMatchers.model().attribute("myList", myListExpected));
    }
}
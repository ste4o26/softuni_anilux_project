package anilux.anilux_spring_mvc.web.controllers;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.binding_models.AnimeBindingModel;
import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.entities.Season;
import anilux.anilux_spring_mvc.domain.view_models.AnimeViewModel;
import anilux.anilux_spring_mvc.domain.view_models.DetailAnimeViewModel;
import anilux.anilux_spring_mvc.domain.view_models.GenreViewModel;
import anilux.anilux_spring_mvc.repositories.AnimeRepository;
import anilux.anilux_spring_mvc.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

@AutoConfigureMockMvc
class AnimeControllerTest extends BaseTest {
    List<Genre> genres;

    Anime anime;

    @MockBean
    AnimeRepository animeRepository;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setup() {
        genres = new ArrayList<>();

        Genre genre1 = new Genre();
        genre1.setName("Shounen");
        genres.add(genre1);

        Genre genre2 = new Genre();
        genre2.setName("Magic");
        genres.add(genre2);

        Season season = new Season();
        season.setEpisodes(new HashSet<>());

        anime = new Anime();
        anime.setId(1L);
        anime.setName("Naruto");
        anime.setGenres(Set.of(genres.get(0)));
        anime.setSummary("Some Test Summary...");
        anime.setImageThumbnailUrl("/img/some/test/url.com");
        anime.setTrailerUrl("/video/some/test/url.com");
        anime.setSeasons(Set.of(season));

        Mockito.when(animeRepository.findById(1L))
                .thenReturn(Optional.of(anime));

        Mockito.when(genreRepository.findAll(Sort.by("name")))
                .thenReturn(genres);
    }

    @WithMockUser("spring")
    @Test
    public void getById_whenIdIsCorrect_shouldReturnItSuccessfully() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/animes/" + anime.getId()))
                .andExpect(MockMvcResultMatchers.view().name("anime"))
                .andExpect(MockMvcResultMatchers.model().attribute("anime", modelMapper.map(anime, DetailAnimeViewModel.class)));
    }

    @WithMockUser("spring")
    @Test
    public void getById_whenIdIsNOTCorrect_shouldReturnErrorPage() throws Exception {
        Mockito.when(animeRepository.findById(2L))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/animes/2"))
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @WithMockUser(value = "spring", roles = {"ADMIN"})
    @Test
    public void uploadGET_whenUserIsAdmin_shouldReturnItSuccessfully() throws Exception {
        List<GenreViewModel> expected = collectionMapperUtil.map(genres, GenreViewModel.class);

        mvc.perform(MockMvcRequestBuilders.get("/animes/upload"))
                .andExpect(MockMvcResultMatchers.view().name("add-anime"))
                .andExpect(MockMvcResultMatchers.model().attribute("animeBindingModel", new AnimeBindingModel()))
                .andExpect(MockMvcResultMatchers.model().attribute("hasImage", true))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", expected));
    }

    @WithMockUser(value = "spring")
    @Test
    public void uploadGET_whenIsNOTAdmin_shouldReturnErrorPage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/animes/upload"))
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @WithMockUser("spring")
    @Test
    public void getAllByPage_whenAllArgumentsPresent_shouldReturnItSuccessfully() throws Exception {
        anime.setLikes(1);

        PageImpl<Anime> animesPage = new PageImpl<Anime>(List.of(anime));
        Mockito.when(animeRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(animesPage);

        List<AnimeViewModel> allAnimesExpected = collectionMapperUtil.map(List.of(anime), AnimeViewModel.class);

        mvc.perform(MockMvcRequestBuilders.get("/animes/all")
                .param("type", "MOST_POPULAR")
                .param("pageNumber", "1"))
                .andExpect(MockMvcResultMatchers.view().name("all-animes"))
                .andExpect(MockMvcResultMatchers.model().attribute("allAnimes", allAnimesExpected))
                .andExpect(MockMvcResultMatchers.model().attribute("animesType", "MOST_POPULAR"))
                .andExpect(MockMvcResultMatchers.model().attribute("pageNum", 1));
    }

//    @Test
//    public void getByTitle() {
//
//    }
}
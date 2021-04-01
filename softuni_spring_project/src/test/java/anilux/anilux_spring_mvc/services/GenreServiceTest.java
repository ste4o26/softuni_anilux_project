package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.GenreViewModel;
import anilux.anilux_spring_mvc.exceptions.GenreAlreadyExistsException;
import anilux.anilux_spring_mvc.repositories.GenreRepository;
import anilux.anilux_spring_mvc.services.interfaces.GenreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenreServiceTest extends BaseTest {
    Genre genre;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    GenreService service;

    @BeforeEach
    public void setup() {
//        Anime anime = new Anime();
//        anime.setId(1L);
//        anime.setName("Boku No Hero Academia");
//        anime.setSummary("Some summery just in case...");
//        anime.setImageThumbnailUrl("/img/2.jpg");
//        anime.setReleaseDate(LocalDate.of(2021, 1, 27));
//        anime.setCompleted(false);
//        anime.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
//        genre = new Genre("Shounen", Set.of(anime));


        genre = new Genre();
        genre.setId(1L);
        genre.setName("Shounen");
    }

    @AfterEach
    void reset() {
        Mockito.reset(genreRepository);
    }

    @Test
    public void persist_whenIsValidGenre_shouldPersistSuccessfully() {
        Mockito.lenient().when(genreRepository.findByName("Shounen"))
                .thenThrow(EntityNotFoundException.class);

        Mockito.reset(genreRepository);
        GenreServiceModel genreServiceModel = modelMapper.map(genre, GenreServiceModel.class);

        service.persist(genreServiceModel);

        Mockito.verify((genreRepository), Mockito.times(1))
                .saveAndFlush(genre);
    }

    @Test
    public void persist_whenIsINVALIDGenre_shouldTrowException() {
        Mockito.lenient().when(genreRepository.findByName("Shounen"))
                .thenThrow(EntityNotFoundException.class);

        GenreServiceModel genreServiceModel = new GenreServiceModel();
        assertThrows(IllegalArgumentException.class, () -> service.persist(genreServiceModel));
    }

    @Test
    public void persist_whenIsExistingGenre_shouldTrowException() {
        Mockito.lenient().when(genreRepository.findByName("Shounen"))
                .thenReturn(Optional.of(genre));

        GenreServiceModel genreServiceModel = modelMapper.map(genre, GenreServiceModel.class);

        assertThrows(GenreAlreadyExistsException.class, () -> service.persist(genreServiceModel));
    }

    @Test
    public void fetchById_whenExistingGenre_shouldReturnIt() {
        Mockito.lenient().when(genreRepository.findById(1L))
                .thenReturn(Optional.of(genre));

        GenreServiceModel genreServiceModel = service.fetchById(1L);

        assertEquals(1, genreServiceModel.getId());
        assertEquals("Shounen", genreServiceModel.getName());
    }

    @Test
    public void fetchById_whenNOTExistingGenre_shouldThrowException() {
        Mockito.lenient().when(genreRepository.findById(1L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.fetchById(1L));
    }

    private List<Genre> setUpListOfGenres() {
        List<Genre> genres = new ArrayList<>();

        Genre genre1 = new Genre();
        genre1.setId(2L);
        genre1.setName("Magic");
        genres.add(genre1);

        genres.add(genre);

        Genre genre2 = new Genre();
        genre2.setId(3L);
        genre2.setName("Ski-Fi");
        genres.add(genre2);

        return genres;
    }

    @Test
    public void fetchAllSortedByName_whenPresentGenresInDB_shouldReturnThemAllSortedByName() {
        List<Genre> genres = this.setUpListOfGenres();

        Mockito.when(genreRepository.findAll(Sort.by("name")))
                .thenReturn(genres);

        List<GenreViewModel> actual = service.fetchAllSortedByName();

        assertEquals(3, actual.size());
        assertEquals("Magic", actual.get(0).getName());
        assertEquals("Ski-Fi", actual.get(2).getName());
    }

    @Test
    public void fetchAllSortedByName_whenEmptyDB_shouldReturnEmptyList() {
        List<Genre> genres = new ArrayList<>();

        Mockito.when(genreRepository.findAll(Sort.by("name")))
                .thenReturn(genres);

        List<GenreViewModel> actual = service.fetchAllSortedByName();

        assertEquals(0, actual.size());
        assertTrue(actual.isEmpty());
    }
}
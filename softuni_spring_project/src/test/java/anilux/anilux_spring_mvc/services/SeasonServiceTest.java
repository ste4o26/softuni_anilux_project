package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Genre;
import anilux.anilux_spring_mvc.domain.entities.Season;
import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;
import anilux.anilux_spring_mvc.repositories.SeasonRepository;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SeasonServiceTest extends BaseTest {
    Season season;

    Anime anime;

    @MockBean
    SeasonRepository seasonRepository;

    @Autowired
    SeasonService service;

    @BeforeEach
    void setup() {
        anime = new Anime();
        anime.setId(1L);
        anime.setName("Boku No Hero Academia");
        anime.setSummary("Some summery just in case...");
        anime.setImageThumbnailUrl("/img/2.jpg");
        anime.setReleaseDate(LocalDate.of(2021, 1, 27));
        anime.setCompleted(false);
        anime.setTrailerUrl("https://www.youtube.com/embed/k4iTICgLOtw");
        Genre genre = new Genre();
        genre.setName("Shounen");
        anime.setGenres(Set.of(genre));

        season = new Season();
    }


    @AfterEach
    void reset() {
        Mockito.reset(seasonRepository);
    }

    @Test
    public void persist_whenValidAnimeAndFirstSeason_shouldPersistNewSeasonForIt() {
        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);

        service.persist(animeServiceModel);

        Mockito.verify(seasonRepository, Mockito.times(1))
                .saveAndFlush(season);
    }

    @Test
    public void persist_whenValidAnimeAndNextSeason_shouldPersistNewSeasonForIt() {
//        Mockito.reset(seasonRepository);
        season.setNumber(1);
        season.setAnime(anime);

        HashSet<Season> seasons = new HashSet<>();
        seasons.add(season);

        anime.setSeasons(seasons);
        AnimeServiceModel animeServiceModel = modelMapper.map(anime, AnimeServiceModel.class);

        service.persist(animeServiceModel);

        Mockito.verify(seasonRepository, Mockito.times(1))
                .saveAndFlush(season);

        ArgumentCaptor<Season> argument = ArgumentCaptor.forClass(Season.class);
        Mockito.verify(seasonRepository).saveAndFlush(argument.capture());
        Season actual = argument.getValue();

        assertEquals(season.getAnime(), actual.getAnime());
        assertEquals(2, actual.getNumber());
    }

    @Test
    public void persist_whenINVALIDAnime_shouldThrowException() {
        AnimeServiceModel animeServiceModel = new AnimeServiceModel();
        assertThrows(IllegalArgumentException.class, () -> service.persist(animeServiceModel));
    }

    @Test
    public void delete_whenExistingAnimeWithId_shouldBeDeleted() {
        season.setId(1L);

        Mockito.when(seasonRepository.findById(1L))
                .thenReturn(Optional.of(season));

        SeasonServiceModel actual = service.delete(1L);

        Mockito.verify(seasonRepository, Mockito.times(1)).deleteById(1L);
        assertEquals(1, actual.getId());
    }

    @Test
    public void delete_whenNOTExistingAnimeWithId_shouldThrowException() {
        Mockito.when(seasonRepository.findById(1L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    public void fetchById_whenExistingSeasonWithId_shouldReturnIt() {
        season.setId(1L);

        Mockito.when(seasonRepository.findById(1L))
                .thenReturn(Optional.of(season));

        SeasonServiceModel actual = service.fetchById(1L);

        assertEquals(1, actual.getId());
        assertEquals(season.getNumber(), actual.getNumber());
    }

    @Test
    public void fetchById_whenNOTExistingSeasonWithId_shouldThrowException() {
        Mockito.when(seasonRepository.findById(1L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> service.fetchById(1L));
    }
}
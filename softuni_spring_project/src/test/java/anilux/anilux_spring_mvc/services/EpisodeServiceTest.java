package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.base.BaseTest;
import anilux.anilux_spring_mvc.domain.entities.Anime;
import anilux.anilux_spring_mvc.domain.entities.Episode;
import anilux.anilux_spring_mvc.domain.entities.Season;
import anilux.anilux_spring_mvc.domain.service_models.EpisodeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.SeasonServiceModel;
import anilux.anilux_spring_mvc.repositories.EpisodeRepository;
import anilux.anilux_spring_mvc.repositories.SeasonRepository;
import anilux.anilux_spring_mvc.services.interfaces.EpisodeService;
import anilux.anilux_spring_mvc.services.interfaces.SeasonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EpisodeServiceTest extends BaseTest {
    Season season;

    Episode episode;

    @MockBean
    SeasonRepository seasonRepository;

    @MockBean
    EpisodeRepository episodeRepository;

    @Autowired
    SeasonService seasonService;

    @Autowired
    EpisodeService service;

    @BeforeEach
    void setup() {
        Anime anime = new Anime();
        anime.setName("Black Clover");

        season = new Season(1, false, anime, new HashSet<>());
        season.setId(1L);

        episode = new Episode();
        episode.setEpisodeUrl("some/Test/url.com");
        episode.setSeason(season);
    }

    @AfterEach
    void reset() {
        Mockito.reset(episodeRepository, seasonRepository);
    }

    @Test
    public void persist_whenIsINVALIDEpisode_shouldThrowException() {
        EpisodeServiceModel episodeServiceModel = new EpisodeServiceModel();
        episodeServiceModel.setSeason(modelMapper.map(season, SeasonServiceModel.class));

        Mockito.when(seasonRepository.findById(1L))
                .thenReturn(Optional.of(season));

        assertThrows(IllegalArgumentException.class, () -> service.persist(episodeServiceModel));
    }

    @Test
    public void persist_whenIsValidEpisode_shouldPersistItSuccessfully() {
        EpisodeServiceModel episodeServiceModel = modelMapper.map(episode, EpisodeServiceModel.class);

        Mockito.when(seasonRepository.findById(1L))
                .thenReturn(Optional.of(season));

        service.persist(episodeServiceModel);

        Mockito.verify(episodeRepository, Mockito.times(1)).saveAndFlush(episode);
    }

    @Test
    public void fetchById_whenIsExistingEpisodeWithId_shouldReturnIt() {
        Mockito.when(episodeRepository.findById(1L))
                .thenReturn(Optional.of(episode));

        episode.setId(1L);

        EpisodeServiceModel actual = service.fetchById(1L);

        assertEquals(1, actual.getId());
        assertEquals(modelMapper.map(episode, EpisodeServiceModel.class), actual);
    }

    @Test
    public void fetchById_whenIsNOTExistingEpisodeWithId_shouldReturnIt() {
        Mockito.when(episodeRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.fetchById(1L));
    }

    @Test
    public void fetchBySeasonIdAndNumber_whenIsExistingEpisodeAndAreValidArguments_shouldReturnIt() {
        episode.setNumber(1);
        episode.setSeason(season);

        Mockito.when(episodeRepository.findBySeasonIdAndNumber(1L, 1))
                .thenReturn(Optional.of(episode));

        EpisodeServiceModel actual = service.fetchBySeasonIdAndNumber(1L, 1);

        assertEquals(1L, actual.getSeason().getId());
        assertEquals(1, actual.getNumber());
        assertEquals(modelMapper.map(episode, EpisodeServiceModel.class), actual);
    }

    @Test
    public void fetchBySeasonIdAndNumber_whenIsNOTExistingEpisodeOrAreNOTValidArguments_shouldThrowException() {
        Mockito.when(episodeRepository.findBySeasonIdAndNumber(1L, 1))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.fetchBySeasonIdAndNumber(1L, 1));
    }
}
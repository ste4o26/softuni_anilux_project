package anilux.anilux_spring_mvc.repositories;

import anilux.anilux_spring_mvc.domain.entities.Anime;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AnimeRepositoryTest {
    private static final String TEST_STRING = "DUMMY STRING";

    @Autowired
    AnimeRepository animeRepository;

    @BeforeEach
    public void setUp() {
        Anime testAnime1 = new Anime();
        testAnime1.setName("Shokugeki no Soma");
        testAnime1.setLikes(1);
        testAnime1.setReleaseDate(LocalDate.now());
        testAnime1.setSummary(TEST_STRING);
        testAnime1.setImageThumbnailUrl(TEST_STRING);
        testAnime1.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime1);

        Anime testAnime2 = new Anime();
        testAnime2.setName("Haikyuu!");
        testAnime2.setLikes(2);
        testAnime2.setReleaseDate(LocalDate.now());
        testAnime2.setSummary(TEST_STRING);
        testAnime2.setImageThumbnailUrl(TEST_STRING);
        testAnime2.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime2);

        Anime testAnime3 = new Anime();
        testAnime3.setName("Death Note");
        testAnime3.setLikes(3);
        testAnime3.setReleaseDate(LocalDate.now());
        testAnime3.setSummary(TEST_STRING);
        testAnime3.setImageThumbnailUrl(TEST_STRING);
        testAnime3.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime3);

        Anime testAnime4 = new Anime();
        testAnime4.setName("Your Lie In April");
        testAnime4.setLikes(4);
        testAnime4.setReleaseDate(LocalDate.now());
        testAnime4.setSummary(TEST_STRING);
        testAnime4.setImageThumbnailUrl(TEST_STRING);
        testAnime4.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime4);

        Anime testAnime5 = new Anime();
        testAnime5.setName("Kuroko no Basket");
        testAnime5.setLikes(5);
        testAnime5.setReleaseDate(LocalDate.now());
        testAnime5.setSummary(TEST_STRING);
        testAnime5.setImageThumbnailUrl(TEST_STRING);
        testAnime5.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime5);

        Anime testAnime6 = new Anime();
        testAnime6.setName("Full Metal Alchemist");
        testAnime6.setLikes(6);
        testAnime6.setReleaseDate(LocalDate.now());
        testAnime6.setSummary(TEST_STRING);
        testAnime6.setImageThumbnailUrl(TEST_STRING);
        testAnime6.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime6);

        Anime testAnime7 = new Anime();
        testAnime7.setName("Naruto");
        testAnime7.setLikes(7);
        testAnime7.setReleaseDate(LocalDate.now());
        testAnime7.setSummary(TEST_STRING);
        testAnime7.setImageThumbnailUrl(TEST_STRING);
        testAnime7.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime7);

        Anime testAnime8 = new Anime();
        testAnime8.setName("Boruto: Naruto Next Generation");
        testAnime8.setLikes(8);
        testAnime8.setReleaseDate(LocalDate.now());
        testAnime8.setSummary(TEST_STRING);
        testAnime8.setImageThumbnailUrl(TEST_STRING);
        testAnime8.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime8);

        Anime testAnime9 = new Anime();
        testAnime9.setLikes(9);
        testAnime9.setName("Seven Deadly Sins");
        testAnime9.setReleaseDate(LocalDate.now());
        testAnime9.setSummary(TEST_STRING);
        testAnime9.setImageThumbnailUrl(TEST_STRING);
        testAnime9.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime9);

        Anime testAnime10 = new Anime();
        testAnime10.setName("Attack on Titans");
        testAnime10.setLikes(10);
        testAnime10.setReleaseDate(LocalDate.now());
        testAnime10.setSummary(TEST_STRING);
        testAnime10.setImageThumbnailUrl(TEST_STRING);
        testAnime10.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime10);

        Anime testAnime11 = new Anime();
        testAnime11.setName("Boku no Hero Academia");
        testAnime11.setLikes(11);
        testAnime11.setReleaseDate(LocalDate.now());
        testAnime11.setSummary(TEST_STRING);
        testAnime11.setImageThumbnailUrl(TEST_STRING);
        testAnime11.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime11);

        Anime testAnime12 = new Anime();
        testAnime12.setName("Black Clover");
        testAnime12.setLikes(12);
        testAnime12.setReleaseDate(LocalDate.now());
        testAnime12.setSummary(TEST_STRING);
        testAnime12.setImageThumbnailUrl(TEST_STRING);
        testAnime12.setTrailerUrl(TEST_STRING);
        animeRepository.saveAndFlush(testAnime12);
    }

    @AfterEach
    public void tearAll() {
        animeRepository.deleteAll();
    }

    @Test
    public void findByNameIgnoreCase_whenIsCorrectAnimeNameCaseInsensitive_shouldReturnOptionalOfAnime() {
        //given

        //when
        Optional<Anime> actualResult1 = animeRepository.findByNameIgnoreCase("shOkuGeKi No sOmA");
        Optional<Anime> actualResult2 = animeRepository.findByNameIgnoreCase("ATTACK ON TITANS");
        Optional<Anime> actualResult3 = animeRepository.findByNameIgnoreCase("naruto");

        //then
        assertEquals("Shokugeki no Soma", actualResult1.get().getName());
        assertEquals("Attack on Titans", actualResult2.get().getName());
        assertEquals("Naruto", actualResult3.get().getName());
    }

    @Test
    public void findByNameIgnoreCase_whenIsNOTCorrectAnimeNameCaseInsensitive_shouldNOTPresentValue() {
        //given

        //when
        Optional<Anime> actualResult1 = animeRepository.findByNameIgnoreCase(null);
        Optional<Anime> actualResult2 = animeRepository.findByNameIgnoreCase("");
        Optional<Anime> actualResult3 = animeRepository.findByNameIgnoreCase("WRONG_ANIME_NAME");

        //then
        assertFalse(actualResult1.isPresent());
        assertFalse(actualResult2.isPresent());
        assertFalse(actualResult3.isPresent());
    }

    @Test
    public void findFirst10ByOrderByLikesDesc_whenMoreThan10AnimesPresentInDB_shouldReturnListOf10AnimesOrderedByLikesInDescendingOrder() {
        //given

        //when
        List<Anime> actualResult = animeRepository.findFirst10ByOrderByLikesDesc();

        //then
        assertEquals(10, actualResult.size());

        assertEquals("Black Clover", actualResult.get(0).getName());
        assertEquals("Boku no Hero Academia", actualResult.get(1).getName());
        assertEquals("Attack on Titans", actualResult.get(2).getName());
    }

//    @Test
//    public void findFirst10ByOrderByLikesDesc_whenNoAnimesPresentInDB_shouldReturnEmptyList() {
//        //given
//        animeRepository.deleteById(1L);
//        animeRepository.deleteById(2L);
//        animeRepository.deleteById(3L);
//        animeRepository.deleteById(4L);
//        animeRepository.deleteById(5L);
//        animeRepository.deleteById(6L);
//
//        //when
//        List<Anime> actualResult = animeRepository.findFirst10ByOrderByLikesDesc();
//
//        //then
//        assertEquals(6, actualResult.size());
//
//        assertEquals("Black Clover", actualResult.get(0).getName());
//        assertEquals("Boku no Hero Academia", actualResult.get(1).getName());
//        assertEquals("Attack on Titans", actualResult.get(2).getName());
//    }

    @Test
    public void findFirst10ByOrderByLikesDesc_whenLessThan10AnimesPresentInDB_shouldReturnListOfAllAnimesSortedByLikesInDescendingOrder() {
        //given
        animeRepository.deleteAll();

        //when
        List<Anime> actualResult = animeRepository.findFirst10ByOrderByLikesDesc();

        //then
        assertEquals(0, actualResult.size());
    }


}
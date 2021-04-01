package anilux.anilux_spring_mvc.init;

import anilux.anilux_spring_mvc.domain.entities.enums.RoleName;
import anilux.anilux_spring_mvc.domain.service_models.*;
import anilux.anilux_spring_mvc.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DatabaseInit implements CommandLineRunner {
    private final AnimeService animeService;
    private final EpisodeService episodeService;
    private final GenreService genreService;
    private final UserService userService;
    private final RoleService roleService;
    private final SeasonService seasonService;
    private final CommentService commentService;

    @Autowired
    public DatabaseInit(AnimeService animeService, EpisodeService episodeService, GenreService genreService, UserService userService, RoleService roleService, SeasonService seasonService, CommentService commentService) {
        this.animeService = animeService;
        this.episodeService = episodeService;
        this.genreService = genreService;
        this.userService = userService;
        this.roleService = roleService;
        this.seasonService = seasonService;
        this.commentService = commentService;
    }

    public void init() {
//        List<RoleServiceModel> roles = List.of(
//                new RoleServiceModel("ROOT_ADMIN"),
//                new RoleServiceModel("ADMIN"),
//                new RoleServiceModel("USER")
//        );
//        roles.forEach(this.roleService::persistRole);
//
//        List<UserServiceModel> users = List.of(
//                new UserServiceModel("Ste4o26", "12345", Set.of(this.roleService.getById(1L), this.roleService.getById(3L))),
//                new UserServiceModel("Delyan", "123", Set.of(this.roleService.getById(3L))),
//                new UserServiceModel("Dokito", "456", Set.of(this.roleService.getById(2L))),
//                new UserServiceModel("SlaviBosa", "12345", Set.of(this.roleService.getById(3L))),
//                new UserServiceModel("HerkiTo", "12345", Set.of(this.roleService.getById(3L)))
//        );
//        users.forEach(this.userService::registerUser);
//
//        List<GenreServiceModel> genres = List.of(new GenreServiceModel("Action"),
//                new GenreServiceModel("Super power"),
//                new GenreServiceModel("Ski-Fi"),
//                new GenreServiceModel("Ecchi"),
//                new GenreServiceModel("Shounen"),
//                new GenreServiceModel("Magic"),
//                new GenreServiceModel("Adventure"),
//                new GenreServiceModel("Isekai"),
//                new GenreServiceModel("Shoujo"),
//                new GenreServiceModel("Sport")
//        );
//        genres.forEach(this.genreService::persistGenre);
//
//        List<EpisodeServiceModel> episodes = List.of(
//                new EpisodeServiceModel(1, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 15, LocalDate.of(2015, 12, 1)),
//                new EpisodeServiceModel(138, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 15, LocalDate.of(2018, 8, 18)),
//                new EpisodeServiceModel(24, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 18, LocalDate.of(2020, 1, 13)),
//                new EpisodeServiceModel(32, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 1, LocalDate.of(2019, 1, 1)),
//                new EpisodeServiceModel(12, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 28, LocalDate.of(2019, 1, 2)),
//                new EpisodeServiceModel(118, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 42, LocalDate.of(2010, 5, 8)),
//                new EpisodeServiceModel(12, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 192, LocalDate.of(2002, 8, 7)),
//                new EpisodeServiceModel(17, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 1820, LocalDate.of(2010, 7, 10)),
//                new EpisodeServiceModel(24, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 354, LocalDate.of(2010, 3, 15)),
//                new EpisodeServiceModel(16, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 205, LocalDate.of(2009, 1, 28)),
//                new EpisodeServiceModel(3, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 777, LocalDate.of(2013, 2, 26)),
//                new EpisodeServiceModel(7, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 12, LocalDate.of(2014, 11, 18)),
//                new EpisodeServiceModel(4, "/video/Boruto-Naruto-Next-Generations-Episode-183.mp4", 16, LocalDate.of(2011, 6, 20))
//        );
//        episodes.forEach(this.episodeService::persist);
//
//        List<AnimeServiceModel> animes = List.of(
//                new AnimeServiceModel("Black Clover",
//                        "Some summery just in case...",
//                        "/img/11.jpg",
//                        LocalDate.of(2018, 10, 21),
//                        false,
//                        10,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(1L), this.genreService.getById(5L), this.genreService.getById(6L))),
////                        Set.of(this.seasonService.fetchById(1L))),
//
//                new AnimeServiceModel("Shokugeki no Soma",
//                        "Some summery just in case...",
//                        "/img/1.jpg",
//                        LocalDate.of(2016, 8, 18),
//                        false,
//                        15,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(1L), this.genreService.getById(2L))),
////                        Set.of(this.seasonService.fetchById(2L))),
//
//                new AnimeServiceModel("Haikyuu!",
//                        "Some summery just in case...",
//                        "/img/5.jpg",
//                        LocalDate.of(2015, 2, 26),
//                        false,
//                        24,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(2L), this.genreService.getById(7L))),
////                        Set.of(this.seasonService.fetchById(3L))),
//
//                new AnimeServiceModel("Boruto Naruto Next Generation",
//                        "Some summery just in case...",
//                        "/img/7.jpg",
//                        LocalDate.of(2018, 6, 12),
//                        false,
//                        852,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(3L), this.genreService.getById(5L), this.genreService.getById(8L))),
////                        Set.of(this.seasonService.fetchById(4L))),
//
//                new AnimeServiceModel("Naruto",
//                        "Some summery just in case...",
//                        "/img/9.jpg",
//                        LocalDate.of(2005, 2, 26),
//                        true,
//                        1824,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(1L), this.genreService.getById(2L))),
////                        Set.of(this.seasonService.fetchById(5L))),
//
//                new AnimeServiceModel("Attack On Titans",
//                        "Some summery just in case...",
//                        "/img/6.jpg",
//                        LocalDate.of(2012, 10, 13),
//                        true,
//                        357,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(1L), this.genreService.getById(2L), this.genreService.getById(7L), this.genreService.getById(8L))),
////                        Set.of(this.seasonService.fetchById(6L))),
//
//                new AnimeServiceModel("Sword Art Online",
//                        "Some summery just in case...",
//                        "/img/8.jpg",
//                        LocalDate.of(2011, 2, 27),
//                        true,
//                        246,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(3L), this.genreService.getById(4L), this.genreService.getById(7L))),
////                        Set.of(this.seasonService.fetchById(7L))),
//
//                new AnimeServiceModel("Seven Deadly Sins",
//                        "Some summery just in case...",
//                        "/img/10.png",
//                        LocalDate.of(2014, 5, 24),
//                        true,
//                        1054,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(6L), this.genreService.getById(4L), this.genreService.getById(2L))),
////                        Set.of(this.seasonService.fetchById(8L))),
//
//                new AnimeServiceModel("Tower Of God",
//                        "Some summery just in case...",
//                        "/img/3.jpg",
//                        LocalDate.of(2020, 7, 15),
//                        true,
//                        147,
//                        "https://www.youtube.com/embed/k4iTICgLOtw",
//                        Set.of(this.genreService.getById(3L)))
////                        Set.of(this.seasonService.fetchById(9L)))
//        );
//        animes.forEach(this.animeService::persist);
//
//        List<CommentServiceModel> comments = List.of(
//                new CommentServiceModel("Some Comment just in case...", this.userService.getById(1L), this.episodeService.fetchById(3L)),
//                new CommentServiceModel("Some Comment just in case...", this.userService.getById(2L), this.episodeService.fetchById(1L)),
//                new CommentServiceModel("Some Comment just in case...", this.userService.getById(3L), this.episodeService.fetchById(4L))
//        );
//
//        comments.forEach(this.commentService::persist);
//
//        //todo for now they are all but this may change while im putting more records!
//        List<AnimeServiceModel> allAnimes = this.animeService.fetchFirstTenMostPopular();
//
//        List<SeasonServiceModel> seasons = List.of(
//                new SeasonServiceModel(1, allAnimes.get(0), Set.of(this.episodeService.fetchById(1L), this.episodeService.fetchById(2L))),
//                new SeasonServiceModel(1, allAnimes.get(1), Set.of(this.episodeService.fetchById(3L))),
//                new SeasonServiceModel(1, allAnimes.get(2), Set.of(this.episodeService.fetchById(4L), this.episodeService.fetchById(5L))),
//                new SeasonServiceModel(1, allAnimes.get(3), Set.of(this.episodeService.fetchById(6L), this.episodeService.fetchById(2L))),
//                new SeasonServiceModel(1, allAnimes.get(4), Set.of(this.episodeService.fetchById(7L), this.episodeService.fetchById(8L))),
//                new SeasonServiceModel(1, allAnimes.get(5), Set.of(this.episodeService.fetchById(1L), this.episodeService.fetchById(9L))),
//                new SeasonServiceModel(1, allAnimes.get(4), Set.of(this.episodeService.fetchById(10L))),
//                new SeasonServiceModel(1, allAnimes.get(6), Set.of(this.episodeService.fetchById(11L))),
//                new SeasonServiceModel(1, allAnimes.get(6), Set.of(this.episodeService.fetchById(12L))),
//                new SeasonServiceModel(1, allAnimes.get(2), Set.of(this.episodeService.fetchById(13L)))
//        );
//        seasons.forEach(this.seasonService::persistSeason);
    }

    private void initRoles() {
        Arrays.stream(RoleName.values())
                .forEach(roleName -> this.roleService.persist(new RoleServiceModel(roleName)));
    }

    private void initGenres() {
        Set.of(
                new GenreServiceModel("Seinen"),
                new GenreServiceModel("Isekai"),
                new GenreServiceModel("Supernatural"),
                new GenreServiceModel("Magic"),
                new GenreServiceModel("Ski-Fi"),
                new GenreServiceModel("Shounen")
        ).forEach(this.genreService::persist);
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.roleService.isEmpty()) {
            this.initRoles();
        }

        if (this.genreService.isEmpty()) {
            this.initGenres();
        }
    }
}

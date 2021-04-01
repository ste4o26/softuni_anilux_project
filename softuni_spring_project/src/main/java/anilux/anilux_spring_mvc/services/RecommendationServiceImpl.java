package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.service_models.AnimeServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.GenreServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.domain.view_models.AnimeViewModel;
import anilux.anilux_spring_mvc.domain.view_models.UserViewModel;
import anilux.anilux_spring_mvc.services.interfaces.AnimeService;
import anilux.anilux_spring_mvc.services.interfaces.RecommendationService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import anilux.anilux_spring_mvc.utils.ComparatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final UserService userService;
    private final AnimeService animeService;
    private final ModelMapper modelMapper;
    private final CollectionMapperUtil collectionMapperUtil;

    @Autowired
    public RecommendationServiceImpl(UserService userService, AnimeService animeService, ModelMapper modelMapper, CollectionMapperUtil collectionMapperUtil) {
        this.userService = userService;
        this.animeService = animeService;
        this.modelMapper = modelMapper;
        this.collectionMapperUtil = collectionMapperUtil;
    }

    @Cacheable(cacheNames = "users")
    @Override
    public UserViewModel recommendAnimes(String username) {
        UserServiceModel userServiceModel = this.userService.fetchByUsername(username);
        UserViewModel userViewModel = this.modelMapper.map(userServiceModel, UserViewModel.class);

        Set<AnimeServiceModel> recommendedServiceModels = this.getRecommended(userServiceModel.getMyList());
        List<AnimeViewModel> recommended = this.collectionMapperUtil.map(recommendedServiceModels, AnimeViewModel.class);

        userViewModel.setRecommended(recommended);

        return userViewModel;
    }

    private Set<AnimeServiceModel> getRecommended(Set<AnimeServiceModel> myList) {
        Map<String, Integer> map = new HashMap<>();
        ComparatorUtil comparator = new ComparatorUtil(map);
        Map<String, Integer> genresPreferenceLevel = new TreeMap<>(comparator);

        for (AnimeServiceModel anime : myList) {

            Set<GenreServiceModel> genres = anime.getGenres();
            for (GenreServiceModel genre : genres) {

                String key = genre.getName();
                if (map.containsKey(key)) {
                    Integer value = map.get(key) + 1;
                    map.put(key, value);

                } else {
                    map.put(key, 1);
                }

            }
        }

        genresPreferenceLevel.putAll(map);
        Integer highestPreferenceValue = this.getHighestPreferenceValue(genresPreferenceLevel);

        Set<AnimeServiceModel> recommended = new LinkedHashSet<>();
        for (Map.Entry<String, Integer> entry : genresPreferenceLevel.entrySet()) {
            if (!(entry.getValue() <= highestPreferenceValue / 2)) {
                Set<AnimeServiceModel> animesWithGenre = this.animeService.fetchAllWithGenre(entry.getKey());

                for (AnimeServiceModel anime : animesWithGenre) {
                    if (!myList.contains(anime)) {
                        recommended.add(anime);
                    }
                }
            }
        }

        return recommended;
    }

    private Integer getHighestPreferenceValue(Map<String, Integer> preferences) {
        int highestValue = -1;
        for (Map.Entry<String, Integer> entry : preferences.entrySet()) {
            if (highestValue < entry.getValue()) {
                highestValue = entry.getValue();
            }
        }

        return highestValue;
    }

//    @Scheduled(cron = "* 0 8 * * *")
    @Scheduled(cron = "* */5 * * * *")
//    @Scheduled(cron = "50 * * * * *")
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void enableCashing() {
        int b = 5;
        System.out.println("CLEARING CACHE!!!");
    }
}

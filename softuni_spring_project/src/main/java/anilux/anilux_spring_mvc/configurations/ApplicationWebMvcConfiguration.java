package anilux.anilux_spring_mvc.configurations;

import anilux.anilux_spring_mvc.web.interceptors.FavIconInterceptor;
import anilux.anilux_spring_mvc.web.interceptors.UserProfileInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {
    private final FavIconInterceptor favIconInterceptor;
    private final UserProfileInterceptor userProfileInterceptor;

    @Autowired
    public ApplicationWebMvcConfiguration(FavIconInterceptor favIconInterceptor,
                                          UserProfileInterceptor userProfileInterceptor) {
        this.favIconInterceptor = favIconInterceptor;
        this.userProfileInterceptor = userProfileInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.favIconInterceptor);
        registry.addInterceptor(this.userProfileInterceptor).addPathPatterns("/users/myProfile");
    }
}

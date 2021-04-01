package anilux.anilux_spring_mvc.web.interceptors;

import anilux.anilux_spring_mvc.domain.view_models.UserViewModel;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserProfileInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        UserViewModel userViewModel = (UserViewModel) modelAndView.getModel().get("userViewModel");

        if (userViewModel.getRecommended().size() == 0){
            modelAndView.addObject("hasNoRecommendations", true);
        }
    }
}

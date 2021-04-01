package anilux.anilux_spring_mvc.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FavIconInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

        String faviconUrl = "https://res.cloudinary.com/ste4o26/image/upload/v1616251469/favicon-32x32_s4mmvh.png";

        if (modelAndView != null) {
            modelAndView.addObject("favicon", faviconUrl);
        }
    }
}

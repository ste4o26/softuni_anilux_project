package anilux.anilux_spring_mvc.web.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler
    public ModelAndView exceptionHandler(RuntimeException exception) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error");
        modelAndView.addObject("message", exception.getMessage());

        return modelAndView;
    }
}

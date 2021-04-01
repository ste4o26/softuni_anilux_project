package anilux.anilux_spring_mvc.services;

import anilux.anilux_spring_mvc.domain.service_models.EmailServiceModel;
import anilux.anilux_spring_mvc.domain.service_models.UserServiceModel;
import anilux.anilux_spring_mvc.services.interfaces.EmailService;
import anilux.anilux_spring_mvc.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSenderImpl javaMailSender;
    private final UserService userService;

    @Autowired
    public EmailServiceImpl(JavaMailSenderImpl javaMailSender,
                            UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    @Override
    public void sendEmail(EmailServiceModel emailServiceModel) {
        UserServiceModel userServiceModel = this.userService.fetchByUsername(emailServiceModel.getUsername());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(userServiceModel.getEmail());
        simpleMailMessage.setTo("aniluxapp@gmail.com");
        simpleMailMessage.setSubject(String.format("New feedback sent from %s", userServiceModel.getEmail()));
        simpleMailMessage.setText(emailServiceModel.getContent());

        this.javaMailSender.send(simpleMailMessage);
    }
}

package anilux.anilux_spring_mvc.services.interfaces;

import anilux.anilux_spring_mvc.domain.service_models.EmailServiceModel;

public interface EmailService {

    void sendEmail(EmailServiceModel emailServiceModel);
}

package anilux.anilux_spring_mvc;

import anilux.anilux_spring_mvc.services.interfaces.CloudinaryService;
import anilux.anilux_spring_mvc.services.interfaces.EmailService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    @MockBean
    public EmailService emailService;

    @MockBean
    public CloudinaryService cloudinaryService;
}

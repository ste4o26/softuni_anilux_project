package anilux.anilux_spring_mvc.configurations;

import anilux.anilux_spring_mvc.utils.CollectionMapperUtil;
import anilux.anilux_spring_mvc.utils.CollectionMapperUtilImpl;
import anilux.anilux_spring_mvc.utils.ValidatorUtil;
import anilux.anilux_spring_mvc.utils.ValidatorUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ApplicationBeanConfiguration {
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private SpringSecurityDialect springSecurityDialect;
    private ValidatorUtil validatorUtil;
    private Validator validator;
    private CollectionMapperUtil collectionMapperUtil;

    @Bean
    public ModelMapper modelMapper() {
        if (this.modelMapper == null) {
            this.modelMapper = new ModelMapper();
        }

        return this.modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (this.passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        return this.passwordEncoder;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        if (this.springSecurityDialect == null) {
            this.springSecurityDialect = new SpringSecurityDialect();
        }

        return this.springSecurityDialect;
    }

    @Bean
    public Validator validator() {
        if (this.validator == null) {
            this.validator = Validation
                    .buildDefaultValidatorFactory()
                    .getValidator();
        }

        return this.validator;
    }

    @Bean
    public ValidatorUtil validatorUtil() {
        if (this.validatorUtil == null) {
            this.validatorUtil = new ValidatorUtilImpl(this.validator());
        }

        return this.validatorUtil;
    }

    @Bean
    public CollectionMapperUtil collectionMapperUtil() {
        if (this.collectionMapperUtil == null) {
            this.collectionMapperUtil = new CollectionMapperUtilImpl(this.modelMapper());
        }

        return this.collectionMapperUtil;
    }

//    @Bean
//    public JavaMailSenderImpl javaMailSenderImpl(){
//        return new JavaMailSenderImpl();
//    }
}

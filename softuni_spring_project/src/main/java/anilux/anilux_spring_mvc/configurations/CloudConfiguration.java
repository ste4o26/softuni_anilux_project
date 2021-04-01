package anilux.anilux_spring_mvc.configurations;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfiguration {
    @Value("${com.cloudinary.cloud_name:ste4o26}")
    private String cloudName;

    @Value("${com.cloudinary.api_key:655929197311167}")
    private String apiKey;

    @Value("${com.cloudinary.api_secret:gT5Qj5F_DBFNQi1eleqjpPep5DA}")
    private String apiSecret;

    @Bean
    public Cloudinary createCloudinaryConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", this.cloudName);
        config.put("api_key", this.apiKey);
        config.put("api_secret", this.apiSecret);
        return new Cloudinary(config);
    }
}

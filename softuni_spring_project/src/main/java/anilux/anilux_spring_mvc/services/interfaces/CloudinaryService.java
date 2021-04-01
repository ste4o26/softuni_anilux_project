package anilux.anilux_spring_mvc.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartFile) throws IOException;

    String uploadVideo(MultipartFile multipartFile) throws IOException;
}

package com.blinkitassignment.imageuploader.services.impl;

import com.blinkitassignment.imageuploader.dtos.UserRegRequest;
import com.blinkitassignment.imageuploader.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    String login();
    String registerNewUser(UserRegRequest request);

    String uploadImage(MultipartFile file) throws IOException;

    Image searchAnImage(String imageName);
}

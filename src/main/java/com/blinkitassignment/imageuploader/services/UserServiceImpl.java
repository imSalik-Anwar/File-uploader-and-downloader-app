package com.blinkitassignment.imageuploader.services;

import com.blinkitassignment.imageuploader.dtos.UserRegRequest;
import com.blinkitassignment.imageuploader.exceptions.ImageNotFoundException;
import com.blinkitassignment.imageuploader.exceptions.UserNotFoundException;
import com.blinkitassignment.imageuploader.models.Image;
import com.blinkitassignment.imageuploader.models.User;
import com.blinkitassignment.imageuploader.repositories.ImageRepository;
import com.blinkitassignment.imageuploader.repositories.UserRepository;
import com.blinkitassignment.imageuploader.services.impl.UserService;
import com.blinkitassignment.imageuploader.utils.Compression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final BCryptPasswordEncoder passwordEncoder;
    final ImageRepository imageRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageRepository = imageRepository;
    }

    /**
     * login function extracts username and password from HTTP header and validates the
     * credentials against database and let's user log in.
     * @return a success string message
     */
    @Override
    public String login() {
        return "You have logged in successfully.";
    }

    /**
     * Registers a new user into DB.
     * @param request contains user details. Refer UserRegRequest in dtos package to see the fields
     * @return a success string message
     */
    @Override
    public String registerNewUser(UserRegRequest request) {
        // create a new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .images(new ArrayList<>())
                .build();

        // save user in DB
        userRepository.save(user);
        // return success message
        return "User was registered";
    }

    /**
     * Takes an image (or any file) as input, compresses it and stores into DB.
     * @param file image (or any file i.e. pdf etc.)
     * @return s string message with file name
     * @throws IOException carefully handles exceptions that might occur
     */
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // get user from authentication details
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new UserNotFoundException("User details invalid.");
        } else {
            username = authentication.getName();
        }
        User user = userRepository.findByUsername(username);
        // Convert the uploaded file to a byte array
        byte[] imageData = Compression.compressImage(file.getBytes());
        // generate a file name
        String fileName = generateFileName(user);
        // create an image and save into DB
        Image image = Image.builder()
                .fileName(fileName)
                .imageData(imageData)
                .imageType(file.getContentType())
                .user(user)
                .build();

        Image savedImage = imageRepository.save(image);
        // add image to corresponding user
        user.getImages().add(savedImage);
        userRepository.save(user);
        // return success message
        return "Image was saved : "+savedImage.getFileName();
    }

    /**
     * generate a file name which is unique across DB
     * @param user extracts some fields from user object to use for naming
     * @return return a unique file name
     */
    private String generateFileName(User user) {
        // generate a unique file name following the rule:
        // username + userId + imageNumber
        return user.getName()+"-"+user.getUserId()+"-"+(user.getImages().size()+1);
    }

    /**
     * take image or file name as input and searches the file in relative user's account
     * @param imageName
     * @return image or file to the user
     */
    @Override
    public Image searchAnImage(String imageName) {
        // get user from authentication details
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new UserNotFoundException("User details invalid.");
        } else {
            username = authentication.getName();
        }
        User user = userRepository.findByUsername(username);
        // validate id user has any image or not
        List<Image> images = user.getImages();
        if(images.isEmpty()){
            throw new ImageNotFoundException("You don't have any images.");
        }
        // validate if user has requested image
        /**
         * We can further optimize the search using binary search over the image-count included
         * in the file name of every image. All the images stored in a user's account are numbered
         * in ascending sequence.
         */
        boolean flag = false;
        Image response = null;
        for(Image img : images){
            if(img.getFileName().equals(imageName)){
                response = img;
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new ImageNotFoundException("Image does not exist.");
        }
        //return the image
        return response;
    }
}

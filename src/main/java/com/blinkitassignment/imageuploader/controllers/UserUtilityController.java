package com.blinkitassignment.imageuploader.controllers;

import com.blinkitassignment.imageuploader.models.Image;
import com.blinkitassignment.imageuploader.services.UserServiceImpl;
import com.blinkitassignment.imageuploader.utils.Compression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user-utility")
public class UserUtilityController {

    final UserServiceImpl userService;
    @Autowired
    public UserUtilityController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(){
        try{
            String response = userService.login();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
        try{
            String response = userService.uploadImage(file);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/search-image/image-name/{imageName}")
    public ResponseEntity<?> searchAnImage(@PathVariable("imageName") String imageName){
        try{
            Image response = userService.searchAnImage(imageName);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(response.getImageType()))
                    .body(Compression.decompressImage(response.getImageData()));
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

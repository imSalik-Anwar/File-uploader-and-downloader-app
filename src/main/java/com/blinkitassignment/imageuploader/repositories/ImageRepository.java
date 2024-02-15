package com.blinkitassignment.imageuploader.repositories;

import com.blinkitassignment.imageuploader.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}

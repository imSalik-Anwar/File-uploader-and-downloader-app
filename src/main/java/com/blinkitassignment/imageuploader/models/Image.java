package com.blinkitassignment.imageuploader.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "Images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String fileName;

    String imageType;

    @Column(nullable = false, length = 524288) // 500KB max image size
    @Lob
    byte[] imageData;

    // map image to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

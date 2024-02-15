package com.blinkitassignment.imageuploader.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int userId;

    @Column(nullable = false)
    @Size(min = 4, max = 20)
    String name;

    @Column(nullable = false, unique = true)
    @Email
    String email;

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 10)
    String username;

    @Column(nullable = false)
    @Size(min = 6)
    String password;

    final String role = "ROLE_USER";

    // Other user properties and relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Image> images;
}

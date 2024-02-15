package com.blinkitassignment.imageuploader.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRegRequest {

    String name;

    String email;

    String username;

    String password;
}

package com.example.resumeportalserver.model;

import lombok.*;

import java.io.File;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserFile {

    private UUID id;

    @NonNull
    private String email;

    @NonNull
    private File resume;
}

package com.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -4573923986252126785L;

    private String id;
    private String name;
}

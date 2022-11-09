package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel(value = "user",description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = -4573923986252126785L;

    @ApiModelProperty(value = "ID",name = "id")
    private String id;
    @ApiModelProperty(value = "用户名",name = "name")
    private String name;
}

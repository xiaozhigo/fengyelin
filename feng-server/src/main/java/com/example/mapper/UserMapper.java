package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author: hehai
 * @since: 2022-08-08  16:54
 * @description: TODO
 */
public interface UserMapper extends BaseMapper<User> {
    public User getUserById(@Param("id") Integer id);


    Page<User> selectUserPage(@Param("page") Page<User> page);
}


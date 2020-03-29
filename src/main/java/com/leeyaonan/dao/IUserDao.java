package com.leeyaonan.dao;

import com.leeyaonan.pojo.User;

import java.util.List;

public interface IUserDao {

    // 查询所有用户
    List<User> findAll() throws Exception;

    // 根据条件进行查询
    User findByCondition() throws Exception;
}

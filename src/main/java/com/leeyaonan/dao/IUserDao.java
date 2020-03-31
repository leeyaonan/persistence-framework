package com.leeyaonan.dao;

import com.leeyaonan.pojo.User;

import java.util.List;

public interface IUserDao {

    // 查询所有用户
    List<User> findAll() throws Exception;

    // 根据条件进行查询
    User findByCondition(User user) throws Exception;

    // 插入新用户
    void insert(User user);

    // 修改用户
    void update(User user);

    // 删除用户
    void delete(User user);
}

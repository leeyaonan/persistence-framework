package com.leeyaonan.dao;

import com.leeyaonan.io.Resources;
import com.leeyaonan.pojo.User;
import com.leeyaonan.sqlSession.SqlSession;
import com.leeyaonan.sqlSession.SqlSessionFactory;
import com.leeyaonan.sqlSession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class UserDaoImpl implements IUserDao {
    @Override
    public List<User> findAll() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 调用selectList
        List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }

        return users;
    }

    @Override
    public User findByCondition(User user) throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 调用
        User result = sqlSession.selectOne("user.selectOne", user);
        System.out.println(result);

        return result;
    }
}

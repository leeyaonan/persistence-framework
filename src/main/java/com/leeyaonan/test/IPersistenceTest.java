package com.leeyaonan.test;

import com.leeyaonan.dao.IUserDao;
import com.leeyaonan.io.Resources;
import com.leeyaonan.pojo.User;
import com.leeyaonan.sqlSession.SqlSession;
import com.leeyaonan.sqlSession.SqlSessionFactory;
import com.leeyaonan.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

public class IPersistenceTest {
    private SqlSession sqlSession;


    @Test
    public void testInsert() throws Exception {
        IUserDao userDao = this.sqlSession.getMapper(IUserDao.class);

        // 增加接口
        User user = new User();
        user.setId(5);
        user.setUsername("做作业的小朋友");
        userDao.insert(user);
        List<User> users = userDao.findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        IUserDao userDao = this.sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(5);
        user.setUsername("做完作业真舒乎！");
        userDao.update(user);
        List<User> users = userDao.findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }

    @Test
    public void testDelete() throws Exception {
        IUserDao userDao = this.sqlSession.getMapper(IUserDao.class);

        User user = new User();
        user.setId(5);

        // 删除接口
        userDao.delete(user);
        List<User> users = userDao.findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }

    @Before
    public void init() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        this.sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 调用
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        User result = sqlSession.selectOne("user.selectOne", user);
        System.out.println(result);

        List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void testProxy() throws Exception {
//        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//        SqlSession sqlSession = sqlSessionFactory.openSession();

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }
        User user = userDao.findByCondition(new User(1, "张三"));
        System.out.println(user);

    }

    @Test
    public void testJDBC() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 通过驱动管理类获取数据库连接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rotli?characterEncoding=utf-8", "root", "123456");
            // 定义sql语句
            String sql = "select * from `user` where id = ? and username = ?";
            // 获取预处理statement
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数，第一个参数为sql语句中参数的序号（从1开始），第二个参数为设置的参数值
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "张三");
            // 向数据库发出sql执行查询，查询出结果集
            resultSet = preparedStatement.executeQuery();
            // 遍历查询结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                // 封装user对象
                User user = new User();
                user.setId(id);
                user.setUsername(username);
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

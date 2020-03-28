package com.leeyaonan.test;

import com.leeyaonan.io.Resources;
import com.leeyaonan.sqlSession.SqlSession;
import com.leeyaonan.sqlSession.SqlSessionFactory;
import com.leeyaonan.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class IPersistenceTest {
    private void test() throws PropertyVetoException, DocumentException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
    }
}

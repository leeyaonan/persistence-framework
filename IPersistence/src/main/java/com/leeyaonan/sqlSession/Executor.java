package com.leeyaonan.sqlSession;

import com.leeyaonan.pojo.Configuration;
import com.leeyaonan.pojo.MappedStatement;

import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}

package com.leeyaonan.sqlSession;

import com.leeyaonan.config.BoundSql;
import com.leeyaonan.pojo.Configuration;
import com.leeyaonan.pojo.MappedStatement;
import com.leeyaonan.utils.GenericTokenParser;
import com.leeyaonan.utils.ParameterMapping;
import com.leeyaonan.utils.ParameterMappingTokenHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class SimpleExecutor implements Executor {

    /**
     * 在query方法中编写JDBC代码
     */
    @lombok.SneakyThrows
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) {
        // 1. 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. 获取sql语句：select * from `user` where id = #{id} and username = #{username}
            // 2.1 转换sql语句：select * from `user` where id = ? and username = ?
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        // 3. 获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 4. 设置参数

        // 5. 执行sql

        // 6. 封装返回结果集
    }

    /**
     * 完成对#{}的解析工作，1. 将#{}使用?进行代替； 2. 解析出#{}里面的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        // 标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        // #{}解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return  boundSql;
    }
}

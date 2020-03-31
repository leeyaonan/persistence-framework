package com.leeyaonan.sqlSession;

import com.leeyaonan.pojo.Configuration;
import com.leeyaonan.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {

        // 将要去完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或返回结果过多");
        }
    }

    @Override
    public void insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        simpleExecutor.insert(configuration, mappedStatement, params);
    }

    @Override
    public void update(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        simpleExecutor.update(configuration, mappedStatement, params);
    }

    @Override
    public void delete(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        simpleExecutor.delete(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用jdk动态代理，为Dao层接口生成代理对象，并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            // 匿名内部类
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                // 底层还是调用JDBC代码
                // 根据不同情况选择调用selectList或者selectOne
                // 准备参数
                //      1： statementId，注意！！！在invoke方法中获取不到statementId，
                //          所以使用接口的全限定名作为namespace，
                //          xml文件的namespace必须和接口名保持一致！！！方法id必须和接口名保持一致！！！
                //          即：namespace.id = 接口全限定名.方法名

                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className + "." + methodName;

                if (methodName.contains("insert")) {
                    insert(statementId, args);
                }

                if (methodName.contains("update")) {
                    update(statementId, args);
                }

                if (methodName.contains("delete")) {
                    delete(statementId, args);
                }

                if (methodName.contains("find")) {
                    // 准备参数
                    //      2. params：args

                    // 判断使用selectList还是selectOne
                    // 获取被调用方法的返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    // 判断是否进行了 泛型类型参数化
                    if (genericReturnType instanceof ParameterizedType) {
                        List<Object> objects = selectList(statementId, args);
                        return objects;
                    }
                    return selectOne(statementId, args);
                }
                return null;
            }
        });
        return (T) proxyInstance;
    }
}

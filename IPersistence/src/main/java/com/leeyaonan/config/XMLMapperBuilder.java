package com.leeyaonan.config;

import com.leeyaonan.pojo.Configuration;
import com.leeyaonan.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

        List<Element> selectList = rootElement.selectNodes("//select");
        this.parseMappedStatement(namespace, selectList);
        List<Element> insertList = rootElement.selectNodes("//insert");
        this.parseMappedStatement(namespace, insertList);
        List<Element> updateList = rootElement.selectNodes("//update");
        this.parseMappedStatement(namespace, updateList);
        List<Element> deleteList = rootElement.selectNodes("//delete");
        this.parseMappedStatement(namespace, deleteList);

    }

    private void parseMappedStatement(String namespace, List<Element> list) {
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }
}

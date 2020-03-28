package com.leeyaonan.io;

import java.io.InputStream;

public class Resources {

    // 借用类加载器获取配置文件为二进制流
    public static InputStream getResourceAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}

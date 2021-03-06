package cn.ubibi.jettyboot.demotest.dao.base;


import cn.ubibi.jettyboot.framework.jdbc.*;

public class MyDAO<T> extends DataAccessObject<T> {

    public MyDAO(Class<T> clazz, String tableName) {
        super(clazz, tableName, MyConnectionFactory.getInstance());
    }

}

package cn.ubibi.jettyboot.demotest.service;

import cn.ubibi.jettyboot.demotest.dao.UserDAO;
import cn.ubibi.jettyboot.demotest.entity.UserEntity;
import cn.ubibi.jettyboot.framework.rest.annotation.Service;


@Service
public class UserService {

    public UserEntity getUserById(String id) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.findById(id);
    }

}

package com.example.proxyDao;

import com.example.domain.User;

import java.io.IOException;
import java.util.List;

public interface UserMapper {
    List<User> findAll() throws IOException;

    User findById(int id) throws IOException;

    List<User> findByCondition(User user) throws IOException;

    List<User> findByIds(List<Integer> ids) throws IOException;

    void save(User user) throws IOException;

    List<User> findAllByUserId() throws IOException;

    List<User> findUserAndRoleAll() throws IOException;
}

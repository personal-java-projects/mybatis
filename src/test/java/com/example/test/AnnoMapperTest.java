package com.example.test;

import com.example.annoMapper.UserMapper;
import com.example.domain.User;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class AnnoMapperTest {

    private UserMapper userMapper;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void testAdd() {
        User user = new User();
        user.setUsername("测试数据");
        user.setPassword("123");
        user.setBirthday(new Date());
        userMapper.save(user);
    }

    @Test
    public void testUpdate() throws IOException {
        User user = new User();
        user.setId(7);
        user.setUsername("测试数据修改");
        user.setPassword("abc");
        user.setBirthday(new Date());
        userMapper.update(user);
    }

    @Test
    public void testDelete() throws IOException {
        userMapper.delete(5);
    }

    @Test
    public void testFindById() throws IOException {
        User user = userMapper.findById(3);
        System.out.println(user);
    }

    @Test
    public void testFindAll() throws IOException {
        List<User> all = userMapper.findAll();
        for(User user : all){
            System.out.println(user);
        }
    }
}

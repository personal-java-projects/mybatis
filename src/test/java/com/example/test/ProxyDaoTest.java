package com.example.test;

import com.example.domain.User;
import com.example.proxyDao.UserMapper;

import com.example.utils.MultiplyType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ProxyDaoTest {
    // 生成UserMapper和、SqlSession对象
    public Map<String, Object> generateUserMapperAndSqlSession() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        //获得MyBatis框架生成的UserMapper接口的实现类
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Object> sqlMap = new HashMap<>();

        sqlMap.put("userMapper", userMapper);
        sqlMap.put("sqlSession", sqlSession);

        return sqlMap;
    }


    @Test
    public void testProxyDao() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        //获得MyBatis框架生成的UserMapper接口的实现类
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = userMapper.findAll();
        User user = userMapper.findById(1);

        System.out.println(userList);
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void testSelectByCondition() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 模拟条件user
        User user = new User();
        user.setId(4);
        user.setUsername("zhangsan");
        user.setPassword("123");

        List<User> userList = ((UserMapper) generateUserMapperAndSqlSession().get("userMapper")).findByCondition(user);

        System.out.println("userList: " + userList);
    }

    @Test
    public void testFindByIds() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 模拟ids的数据
        List<Integer> ids = new ArrayList<Integer>();

        ids.add(3);
        ids.add(4);
        ids.add(5);

        List<User> userList = ((UserMapper) generateUserMapperAndSqlSession().get("userMapper")).findByIds(ids);

        System.out.println(userList);
    }

    @Test
    public void testSave() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 创建User
        User user = new User();
        user.setUsername("sdh");
        user.setPassword("123");
        user.setBirthday(new Date());
        // 执行保存
        Map<String, Object> sqlMap = generateUserMapperAndSqlSession();

        UserMapper userMapper = (UserMapper) sqlMap.get("userMapper");
        SqlSession sqlSession = (SqlSession) sqlMap.get("sqlSession");

        userMapper.save(user);
        sqlSession.commit();
    }
}

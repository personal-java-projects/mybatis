package com.example.test;

import com.example.domain.Order;
import com.example.domain.User;
import com.example.proxyDao.OrderMapper;
import com.example.proxyDao.UserMapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        // 获得MyBatis框架生成的OrderMapper接口的实现类
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        Map<String, Object> sqlMap = new HashMap<>();

        sqlMap.put("userMapper", userMapper);
        sqlMap.put("sqlSession", sqlSession);
        sqlMap.put("orderMapper", orderMapper);

        return sqlMap;
    }


    @Test
    public void testProxyDao() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        //获得MyBatis框架生成的UserMapper接口的实现类
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 设置分页参数 当前页+每页显示的条数
        PageHelper.startPage(1,2);

        List<User> userList = userMapper.findAll();
//        User user = userMapper.findById(1);

        for(User user : userList){
            System.out.println(user);
        }

        //其他分页的数据
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        System.out.println("总条数："+pageInfo.getTotal());
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("当前页："+pageInfo.getPageNum());
        System.out.println("上一页："+pageInfo.getPrePage());
        System.out.println("下一页："+pageInfo.getNextPage());
        System.out.println("每页显示的总条数："+pageInfo.getPageSize());
        System.out.println("是否第一页："+pageInfo.isIsFirstPage());
        System.out.println("是否最后一页："+pageInfo.isIsLastPage());

        System.out.println(userList);
//        System.out.println(user);
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

    @Test
    public void testFindOrders() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> sqlMap = generateUserMapperAndSqlSession();

        OrderMapper orderMapper = (OrderMapper) sqlMap.get("orderMapper");
        SqlSession sqlSession = (SqlSession) sqlMap.get("sqlSession");

        List<Order> orderList = orderMapper.findAll();

        for (Order order : orderList) {
            System.out.println(order);
        }

        sqlSession.close();
    }

    @Test
    public void testFindAllByUserId() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> sqlMap = generateUserMapperAndSqlSession();

        UserMapper userMapper = (UserMapper) sqlMap.get("userMapper");
        SqlSession sqlSession = (SqlSession) sqlMap.get("sqlSession");

        List<User> userList = userMapper.findAllByUserId();

        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void testFindUserAndRoleAll() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> sqlMap = generateUserMapperAndSqlSession();

        UserMapper userMapper = (UserMapper) sqlMap.get("userMapper");
        SqlSession sqlSession = (SqlSession) sqlMap.get("sqlSession");

        List<User> userAndRoleAll = userMapper.findUserAndRoleAll();

        for (User user : userAndRoleAll) {
            System.out.println(user);
        }

        sqlSession.close();
    }
}

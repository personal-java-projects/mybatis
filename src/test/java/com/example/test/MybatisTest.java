package com.example.test;

import com.example.domain.User;
import com.example.traditionalDao.UserMapper;
import com.example.traditionalDao.impl.UserMapperImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {

    @Test
    // 查询操作
    public void test1() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //执行sql语句
        List<User> userList = sqlSession.selectList("userMapper.findAll");
        //打印结果
        System.out.println(userList);
        //释放资源
        sqlSession.close();
    }

    @Test
    // 插入操作
    public void test2() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 模拟User对象
        User user = new User();
        user.setUsername("tom");
        user.setPassword("1233");
        //执行sql语句
        int row = sqlSession.insert("userMapper.save", user);
        //打印结果
        System.out.println(row);

        // 查询的时候没有操作表，所以用不到事务
        // 插入数据需要操作表，所以用到事务
        // mybatis默认事务不进行提交，事务不提交，插入操作不生效，数据并不会放到数据库中
        // 原始的jdbc默认事务是提交的
        // 事务提交
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    @Test
    // 更新操作
    public void test3() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象, 传参为true不需要手动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        // 模拟User对象
        User user = new User();
        user.setId(3);
        user.setUsername("lucy");
        user.setPassword("cnmsb");
        //执行sql语句
        int row = sqlSession.update("userMapper.update", user);
        //打印结果
        System.out.println(row);

        //释放资源
        sqlSession.close();
    }

    @Test
    // 删除操作
    public void test4() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //执行sql语句
        int row = sqlSession.delete("userMapper.delete", 1);
        //打印结果
        System.out.println(row);

        // 查询的时候没有操作表，所以用不到事务
        // 插入数据需要操作表，所以用到事务
        // mybatis默认事务不进行提交，事务不提交，插入操作不生效，数据并不会放到数据库中
        // 原始的jdbc默认事务是提交的
        // 事务提交
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    @Test
    // 根据id查询一个用户
    public void test5() throws IOException {
        //加载核心配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //获得sqlSession工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        //获得sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 根据id查询用户
        User user = sqlSession.selectOne("userMapper.findById", 3);

        System.out.println("user: " + user);
        sqlSession.close();
    }

    @Test
    public void testTraditionDao() throws IOException {
        UserMapper userMapper = new UserMapperImpl();
        List<User> all = userMapper.findAll();
        System.out.println(all);
    }
}

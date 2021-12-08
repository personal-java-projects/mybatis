## Mybatis简介

#### 1、原始jdbc操作

* 查询数据

![image-20211208150720774](image-20211208150720774.png)

* 插入数据

  ![image-20211208150832782](image-20211208150832782.png)

#### 2、原始jdbc操作的分析

原始jdbc开发存在的问题如下： 

① 数据库连接创建、释放频繁造成系统资源浪费从而影响系统性能 

② sql 语句在代码中硬编码，造成代码不易维护，实际应用 sql 变化的可能较大，sql 变动需要改变java代码。 

③ 查询操作时，需要手动将结果集中的数据手动封装到实体中。插入操作时，需要手动将实体的数据设置到sql语句的占位 符位置 

应对上述问题给出的解决方案：

 ① 使用数据库连接池初始化连接资源 

② 将sql语句抽取到xml配置文件中 

③ 使用反射、内省等底层技术，自动将实体与表进行属性与字段的自动映射

#### 3、什么是Mybatis

![image-20211208151009278](image-20211208151009278.png)

## Mybatis的快速入门

#### 1、MyBatis开发步骤

MyBatis官网地址：http://www.mybatis.org/mybatis-3/

MyBatis开发步骤： 

① 添加MyBatis的依赖 

② 创建user数据表 

③ 编写User实体类 

④ 编写映射文件UserMapper.xml 

⑤ 编写核心文件SqlMapConfig.xml 

⑥ 编写测试类

#### 2、 环境搭建

1. 导入MyBatis的坐标和其他相关依赖

   ```xml
   <!--mybatis坐标-->
   <dependency>
       <groupId>org.mybatis</groupId>
       <artifactId>mybatis</artifactId>
       <version>3.4.5</version>
   </dependency>
   <!--mysql驱动坐标-->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>5.1.6</version>
       <scope>runtime</scope>
   </dependency>
   <!--单元测试坐标-->
   <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>4.12</version>
       <scope>test</scope>
   </dependency>
   <!--日志坐标-->
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.12</version>
   </dependency>
   ```

2. 创建user数据表

   ![image-20211208151537772](image-20211208151537772.png)

3. 编写User实体

   ```java
   public class User {
       private int id;
       private String username;
       private String password;
       //省略get个set方法
   }
   ```

4. 编写UserMapper映射文件

   ```xml-dtd
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
   PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
   "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="userMapper">
       <select id="findAll" resultType="com.itheima.domain.User">
       	select * from User
       </select>
   </mapper>
   ```

5. 编写MyBatis核心文件（SqlMapConfig.xml ）

   ```xml-dtd
   <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN“ "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
   	<environments default="development">
   		<environment id="development">
   			<transactionManagertype="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="com.mysql.jdbc.Driver"/>
                   <property name="url" value="jdbc:mysql:///test"/>
                   <property name="username" value="root"/><property name="password" 						value="root"/>
               </dataSource>
   	    </environment>
   	</environments>
   	<mappers>
       	<mapper resource="com/example/mapper/UserMapper.xml"/>
       </mappers>
   </configuration>
   ```

#### 3、编写测试代码

```java
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
```

#### 4、知识小结

MyBatis开发步骤： 

① 添加MyBatis的依赖 

② 创建user数据表 

③ 编写User实体类 

④ 编写映射文件UserMapper.xml 

⑤ 编写核心文件SqlMapConfig.xml 

⑥ 编写测试类

##  MyBatis的映射文件概述

![image-20211208152304141](image-20211208152304141.png)

## MyBatis的增删改查操作

#### 1、MyBatis的插入数据操作

1. 编写UserMapper映射文件

   ```xml
   <mapper namespace="userMapper">
       <insert id="add" parameterType="com.example.domain.User">
       	insert into user values(#{id},#{username},#{password})
       </insert>
   </mapper>
   ```

2. 编写插入实体User的代码

   ```java
   InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
   SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
   SqlSession sqlSession = sqlSessionFactory.openSession();
   int insert = sqlSession.insert("userMapper.add", user);
   System.out.println(insert);
   //提交事务
   sqlSession.commit();
   sqlSession.close();
   ```

3. 插入操作注意问题

   * 插入语句使用insert标签
   * 在映射文件中使用parameterType属性指定要插入的数据类型
   * Sql语句中使用#{实体属性名}方式引用实体中的属性值
   * 插入操作使用的API是sqlSession.insert(“命名空间.id”,实体对象);
   * 插入操作涉及数据库数据变化，所以要使用sqlSession对象显示的提交事务， 即sqlSession.commit()

#### 2、MyBatis的修改数据操作

1. 编写UserMapper映射文件

   ```xml
   <mapper namespace="userMapper">
       <update id="update" parameterType="com.example.domain.User">
       	update user set username=#{username},password=#{password} where id=#{id}
       </update>
   </mapper>
   ```

   

2. 编写修改实体User的代码

   ```java
   InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
   SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
   SqlSession sqlSession = sqlSessionFactory.openSession();
   int update = sqlSession.update("userMapper.update", user);
   System.out.println(update);
   sqlSession.commit();
   sqlSession.close();
   ```

3. 修改操作注意问题

   * 修改语句使用update标签
   * 修改操作使用的API是sqlSession.update(“命名空间.id”,实体对象);

#### 3、MyBatis的删除数据操作

1. 编写UserMapper映射文件

   ```xml
   <mapper namespace="userMapper">
       <delete id="delete" parameterType="java.lang.Integer">
       	delete from user where id=#{id}
       </delete>
   </mapper>
   ```

2.  编写删除数据的代码

   ```java
   InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
   SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
   SqlSession sqlSession = sqlSessionFactory.openSession();
   int delete = sqlSession.delete("userMapper.delete",3);
   System.out.println(delete);
   sqlSession.commit();
   sqlSession.close();
   ```

3. 删除操作注意问题

   * 删除语句使用delete标签
   * Sql语句中使用#{任意字符串}方式引用传递的单个参数
   * 删除操作使用的API是sqlSession.delete(“命名空间.id”,Object);

#### 4、知识小结

增删改查映射配置与API：

查询数据：

```java
List userList = sqlSession.selectList("userMapper.findAll");
```

```xml
<select id="findAll" resultType="com.example.domain.User">
	select * from User
</select>
```

添加数据：

```java
sqlSession.insert("userMapper.add", user);
```

```xml
<insert id="add" parameterType="com.itheima.domain.User">
	insert into user values(#{id},#{username},#{password})
</insert>
```

修改数据：

```java
sqlSession.update("userMapper.update", user);
```

```xml
<update id="update" parameterType="com.itheima.domain.User">
	update user set username=#{username},password=#{password} where id=#{id}
</update>
```

删除数据：

```java
sqlSession.delete("userMapper.delete",3);
```

```xml
<delete id="delete" parameterType="java.lang.Integer">
	delete from user where id=#{id}
</delete>
```

## MyBatis核心配置文件概述

#### 1、MyBatis核心配置文件层级关系

![image-20211208153604552](image-20211208153604552.png)

#### 2、MyBatis常用配置解析

1. environments标签

   数据库环境的配置，支持多环境配置

   ![image-20211208153703469](image-20211208153703469.png)

   其中，事务管理器（transactionManager）类型有两种：

   * JDBC：这个配置就是直接使用了JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务作用域。
   * MANAGED：这个配置几乎没做什么。它从来不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如JEE  应用服务器的上下文）。 默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将 closeConnection 属性设置 为 false 来阻止它默认的关闭行为。

   其中，数据源（dataSource）类型有三种：

   * UNPOOLED：这个数据源的实现只是每次被请求时打开和关闭连接。
   * POOLED：这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来。
   * JNDI：这个数据源的实现是为了能在如 EJB 或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置 一个 JNDI 上下文的引用。

2. mapper标签

   该标签的作用是加载映射的，加载方式有如下几种：

   * 使用相对于类路径的资源引用，例如：`<mapperresource="org/mybatis/builder/AuthorMapper.xml"/>`

   * 使用完全限定资源定位符（URL），例如：`<mapperurl="file:///var/mappers/AuthorMapper.xml"/>`

   * 使用映射器接口实现类的完全限定类名，例如：`<mapperclass="org.mybatis.builder.AuthorMapper"/>`

   * 将包内的映射器接口实现全部注册为映射器，例如：

     `<package name="org.mybatis.builder"/>`

3. Properties标签

   实际开发中，习惯将数据源的配置信息单独抽取成一个properties文件，该标签可以加载额外配置的properties文件

   ![image-20211208154437801](image-20211208154437801.png)

4. typeAliases标签

   类型别名是为Java 类型设置一个短的名字。原来的类型名称配置如下

   ![image-20211208154531077](image-20211208154531077.png)

   配置typeAliases，为com.itheima.domain.User定义别名为user

   ![image-20211208154608086](image-20211208154608086.png) ![image-20211208154641195](image-20211208154641195.png)

   上面我们是自定义的别名，mybatis框架已经为我们设置好的一些常用的类型的别名

   ![image-20211208154904316](image-20211208154904316.png)

5.  知识小结

   核心配置文件常用配置：

   1、properties标签：该标签可以加载外部的properties文件

   ![image-20211208155029630](image-20211208155029630.png)

   2、typeAliases标签：设置类型别名

   ![image-20211208155059772](image-20211208155059772.png)

   3、mappers标签：加载映射配置

   ![image-20211208155158977](image-20211208155158977.png)

   4、environments标签：数据源环境配置标签

   ![image-20211208155320214](image-20211208155320214.png)

## MyBatis相应API

#### 1、SqlSession工厂构建器SqlSessionFactoryBuilder

常用API：SqlSessionFactory build(InputStream inputStream) 

通过加载mybatis的核心文件的输入流的形式构建一个SqlSessionFactory对象

```java
String resource = "org/mybatis/builder/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
SqlSessionFactory factory = builder.build(inputStream);
```

其中， Resources 工具类，这个类在 org.apache.ibatis.io 包中。Resources 类帮助你从类路径下、文件系统或 一个 web URL 中加载资源文件。

#### 2、SqlSession工厂对象SqlSessionFactory

SqlSessionFactory 有多个个方法创建 SqlSession 实例。常用的有如下两个：

![image-20211208161242432](image-20211208161242432.png)

#### 3、SqlSession会话对象

SqlSession 实例在 MyBatis 中是非常强大的一个类。在这里你会看到所有执行语句、提交或回滚事务和获取映射器实例的方法。 执行语句的方法主要有：

```java
<T> T selectOne(String statement, Object parameter) 
<E> List<E> selectList(String statement, Object parameter) 
int insert(String statement, Object parameter) 
int update(String statement, Object parameter) 
int delete(String statement, Object parameter)
```

操作事务的方法主要有：

```java
void commit()
void rollback()
```


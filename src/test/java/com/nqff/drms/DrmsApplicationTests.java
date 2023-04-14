package com.nqff.drms;

import com.nqff.drms.mapper.UserMapper;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
class DrmsApplicationTests {

	@Autowired
//	UserMapper userMapper;
	UserService userService;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void selectAllUserTest() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		List<User> users = mapper.selectAll();
		System.out.println(users);
		sqlSession.close();
	}

	@Test
	void selectUserByEmailTest() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		SqlSession sqlSession = sqlSessionFactory.openSession();
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		User user = mapper.selectUserByEmail("123456@123.com");
		System.out.println(user);
		sqlSession.close();
	}

	@Test
	void userExistTest() throws IOException {
//		User user = userMapper.selectUserByEmail("123456@123.com");
//		System.out.println(user);
		boolean res = userService.isUserExisted("1234@123.com");
		System.out.println(res);
	}

	@Test
	void getCodeTest() {
		String s = "928196210@qq.com";
		String res = redisTemplate.opsForValue().get(s);
		System.out.println(res);
	}
}

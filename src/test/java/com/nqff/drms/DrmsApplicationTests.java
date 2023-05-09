package com.nqff.drms;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nqff.drms.algorithm.Algorithm;
import com.nqff.drms.dao.UserDao;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

@SpringBootTest
class DrmsApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Autowired
	private UserDao userDao;

	@Autowired
	private Algorithm algorithm;

	@Test
	void mybatisplusTest() {
		System.out.println(userDao.selectById(1));
	}

	@Test
	void mpInsertTest() {
		User user = new User();
		user.setEmail("111@111.com");
		user.setPassword("12345");
		user.setName("ttt");
		userDao.insert(user);
	}

	@Test
	void mpDeleteTest() {
		userDao.deleteById(3);
	}

	@Test
	void mpWhereTest() {
		String email = null;
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(User::getEmail, email);
		wrapper.eq(false, User::getEmail, null);
		System.out.println(userDao.selectList(wrapper));
	}

	@Test
	void selectAllUserTest() throws IOException {
		System.out.println(userService.list());
	}

	@Test
	void selectUserByEmailTest() throws IOException {
		User user = userService.selectUserByEmail("111@111.com");
		System.out.println(user);
	}

	@Test
	void userExistTest() throws IOException {
		boolean res = userService.isUserExisted("928196210@qq.com");
		System.out.println(res);
	}

	@Test
	void getCodeTest() {
		String s = "928196210@qq.com";
		String res = redisTemplate.opsForValue().get(s);
		System.out.println(res);
	}

	@Test
	void getSimilarExamples(){
		String description = "测试获取相似案例";
		System.out.println(algorithm.getSimilarExample(description));
	}
}

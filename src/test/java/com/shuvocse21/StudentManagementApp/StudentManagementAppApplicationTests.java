package com.shuvocse21.StudentManagementApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentManagementAppApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertThat(context).isNotNull();
		assertThat(context.containsBean("userService")).isTrue();
		assertThat(context.containsBean("securityConfig")).isTrue();
	}
}
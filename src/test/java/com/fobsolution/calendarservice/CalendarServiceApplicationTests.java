package com.fobsolution.calendarservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {CalendarServiceApplication.class})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:config/application-test.yml")
class CalendarServiceApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("test passed ...");
	}

}

package com.doranco.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"CLIENT_URL=http://localhost:5173/",
		"JWT_SECRET=fakesecretfakesecretfakesecretfakesecretfakesecretfakesecret3",
		"URI_MONGO=mongodb+srv://fakeUser:fakePassword@fakeCluster.mongodb.net",
		"DB_MONGO=fakeDbName",
		"AEH_ADMIN_EMAIL=fakeAdmin@domain.com",
		"AEH_ADMIN_PASSWORD=fakePassword",
		"DB_SERVER=jdbc:mysql://localhost:3306/fake_db",
		"DB_LOGIN=fakeUser",
		"DB_PASSWORD=fakePassword"
})
class ProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}

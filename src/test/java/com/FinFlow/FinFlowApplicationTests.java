package com.FinFlow;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinFlowApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection connection = DriverManager.getConnection(
						"jdbc:mysql://${rds.hostname}:${rds.port}/${rds.db.name}",
						"${rds.username}",
						"${rds.password}"
		);

		Assertions.assertNotNull(connection);

		connection.close();
	}

}

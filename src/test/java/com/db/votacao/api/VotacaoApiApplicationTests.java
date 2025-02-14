package com.db.votacao.api;

import com.db.votacao.api.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.data.redis.enabled=false"
})
@Import(TestConfig.class)
@ActiveProfiles("test")
class VotacaoApiApplicationTests {

	@Test
	void contextLoads() {
	}
}

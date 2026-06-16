package com.votacao.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;
import com.votacao.backend.support.IntegrationTestConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(IntegrationTestConfig.class)
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}

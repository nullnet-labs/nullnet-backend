package network.thenull.api;

import org.springframework.boot.SpringApplication;

public class TestNullnetBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(NullnetBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

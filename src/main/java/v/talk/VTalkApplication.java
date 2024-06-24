package v.talk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class VTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(VTalkApplication.class, args);
	}

}

package code.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
//@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)    // This forces Spring to serialize Page as a DTO globally.
public class JpaScenariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaScenariosApplication.class, args);
	}

}

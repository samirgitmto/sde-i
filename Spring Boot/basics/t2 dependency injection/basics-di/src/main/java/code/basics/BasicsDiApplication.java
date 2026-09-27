package code.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import code.basics.conditionalBeanLoading.PaymentConfig;

@SpringBootApplication
//@EnableConfigurationProperties(PaymentConfig.class)
public class BasicsDiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicsDiApplication.class, args);
	}

}

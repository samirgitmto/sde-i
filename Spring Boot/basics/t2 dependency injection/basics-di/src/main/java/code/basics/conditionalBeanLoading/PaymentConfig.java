package code.basics.conditionalBeanLoading;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {
	
	private String provider;
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getProvider() {
		return provider;
	}
	
}	
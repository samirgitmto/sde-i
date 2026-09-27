package code.basics.conditionalBeanLoading;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
//@ConditionalOnProperty(name = "payment.provider", havingValue = "razor")
@Profile("razor")
@Primary
public class RazorPaymentService implements PaymentService {

	public RazorPaymentService() {
		System.err.println("RazorPaymentService created");
	}
	
	@Override
	public String pay(Double amount) {
		System.out.println(amount + " paid through razor");
		return "razor";
	}
	
}

package code.basics.conditionalBeanLoading;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
//@ConditionalOnProperty(name = "payment.provider", havingValue = "stripe")
@Profile("stripe")
//@Qualifier("stripePaymentService")
public class StripePaymentService implements PaymentService {

	public StripePaymentService() {
		System.err.println("StripePaymentService created");
	}
	
	@Override
	public String pay(Double amount) {
		System.out.println(amount + " paid through stripe");
		return "stripe";
	}

}

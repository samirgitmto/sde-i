package code.basics.conditionalBeanLoading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Scenario 1: Conditional Bean Loading Based on Configuration
Scenario:
You're working on a payment service that can integrate with multiple providers like Stripe or Razorpay. Only one of them should be active at a time,
 based on a property like payment.provider=stripe or payment.provider=razorpay in application.yml. How would you design this in Spring Boot so only
 the relevant implementation is loaded and injected?

What the interviewer expects:
Defining a PaymentService interface with multiple implementations

Using:
@ConditionalOnProperty to control bean loading
Or @Profile for profile-based loading (optional discussion)
Clean use of configuration binding using @ConfigurationProperties
Avoiding @Autowired conflicts via conditional bean creation

Follow-up questions:
How would this setup behave in tests?
How would you make it extensible for future providers?
 */

@RestController
public class PaymentController {
	
	@Autowired
//	@Qualifier("stripePaymentService")
	private PaymentService paymentService;
	
//	@PostMapping("pay/{amount}")
	@GetMapping("pay/{amount}")
	public ResponseEntity<String> pay(@PathVariable Double amount) {
		String res = paymentService.pay(amount);
		return ResponseEntity.ok(res);
	}
	
}

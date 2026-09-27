package code.jpa.customQueryJpql;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/native/users")
public class UserControllerNative {

private final UserService userService;
	
	@Autowired
	public UserControllerNative(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<User>> getUsersNative(
			@RequestParam(required = false) LocalDate date) {
		List<User> users = userService.getUsersNative(date);
		return ResponseEntity.ok(users);
	}
	@GetMapping("/summary")
	public ResponseEntity<List<UserSummaryDto>> getUsersSummaryNative(
			@RequestParam(required = false) LocalDate date) {
		List<UserSummaryDto> users = userService.getUsersSummaryNative(date);
		return ResponseEntity.ok(users);
	}
	
}

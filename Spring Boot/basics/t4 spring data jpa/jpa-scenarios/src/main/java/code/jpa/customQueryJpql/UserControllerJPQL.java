package code.jpa.customQueryJpql;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpql/users")
public class UserControllerJPQL {
	
	private final UserService userService;
	
	@Autowired
	public UserControllerJPQL(UserService userService) {
		this.userService = userService;
	}
	
//	By default, Spring expects ISO format (yyyy-MM-dd), like 2025-06-20
	@GetMapping("/")
	public ResponseEntity<List<User>> getUsers(
			@RequestParam(required = false) LocalDate date) {
		List<User> users = userService.getUsers(date);
		return ResponseEntity.ok(users);
	}
	@GetMapping("/summary")
	public ResponseEntity<List<UserSummaryDto>> getUsersSummary(
			@RequestParam(required = false) LocalDate date) {
		List<UserSummaryDto> users = userService.getUsersSummary(date);
		return ResponseEntity.ok(users);
	}
	
	// HATEOAS
	@GetMapping("/sorted/dynamic")
	public ResponseEntity<PagedModel<EntityModel<User>>> getUsersPage(
			@RequestParam(required = false) LocalDate date,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "name, asc") String[] sort,
			PagedResourcesAssembler<User> assembler) {  // Injected automatically
		
		Page<User> users = userService.getUsersPageWithDynamicSorting(date, page, size, sort);
		PagedModel<EntityModel<User>> usersPagedModel = assembler.toModel(users);
		return ResponseEntity.ok(usersPagedModel);
	}
	
	
}
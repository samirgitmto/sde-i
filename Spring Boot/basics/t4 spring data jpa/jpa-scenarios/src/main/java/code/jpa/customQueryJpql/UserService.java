package code.jpa.customQueryJpql;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

	private static final String[] FIRST_NAMES = {"John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Robert", "Steven"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"};
    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "example.com"};
	
	private final UserRepo userRepo;
	
	@Autowired
	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@PostConstruct
	public void insertUsers() {
		List<User> users = new ArrayList<User>(1500);
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			
			String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
			String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
			String name = firstName + " " + lastName;
			String email = firstName + "@" + DOMAINS[random.nextInt(DOMAINS.length)];
			LocalDate today = LocalDate.now();
			LocalDate regDate = today.minus(random.nextInt(365*5), ChronoUnit.DAYS);
			User user = new User(null, email, name, regDate);
			users.add(user);
		}
		userRepo.saveAll(users);
	}
	
	public List<User> getUsers(LocalDate date) {
		if (date==null)
			return this.userRepo.findAll();
		else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()))
			return this.userRepo.findUsersRegisteredAfter(date);
		else
			return null;
			
	}
	
	public List<User> getUsersNative(LocalDate date) {
		if (date==null)
			return this.userRepo.findAll();
		else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()))
			return this.userRepo.findUsersRegisteredAfterNative(date);
		else
			return null;
			
	}

	public List<UserSummaryDto> getUsersSummary(LocalDate date) {
		if (date==null)
			return this.userRepo.findAllUsersSummary();
		else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()))
			return this.userRepo.findUsersSummaryRegisteredAfter(date);
		else
			return null;
	}
	public List<UserSummaryDto> getUsersSummaryNative(LocalDate date) {
		if (date==null)
			return this.userRepo.findAllUsersSummaryNative();
		else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()))
			return this.userRepo.findUsersSummaryRegisteredAfterNative(date);
		else
			return null;
	}

	public Page<User> getUsersPageWithDynamicSorting(LocalDate date, int page, int size, String[] sort) {
		
		Sort sorting = Sort.by(
				sort[1].equalsIgnoreCase("desc")?Sort.Direction.DESC:Sort.Direction.ASC,
				sort[0]); 
		
		Pageable pageable = PageRequest.of(page, size, sorting);
		return userRepo.findUsersPageRegisteredAfterWithDynamicSort(date, pageable);
	}
	
}
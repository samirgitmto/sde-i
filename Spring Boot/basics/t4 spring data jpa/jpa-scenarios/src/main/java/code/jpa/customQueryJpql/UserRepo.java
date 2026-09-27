package code.jpa.customQueryJpql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	@Query(value = "SELECT * FROM user_table WHERE REGISTRATION_DATE >= :registrationDate", nativeQuery = true)
	List<User> findUsersRegisteredAfterNative(LocalDate registrationDate);

	@Query(value = "SELECT name, email FROM user_table", nativeQuery = true)
	List<UserSummaryDto> findAllUsersSummaryNative();
	
	@Query(value = "SELECT name, email FROM user_table WHERE REGISTRATION_DATE >= :regDate", nativeQuery = true)
	List<UserSummaryDto> findUsersSummaryRegisteredAfterNative(@Param(value = "regDate") LocalDate date);

	
	// The name in @Param must match the JPQL parameter (:reg).
	@Query("SELECT u FROM User u WHERE u.registrationDate >= :reg")
	List<User> findUsersRegisteredAfter(@Param(value = "reg") LocalDate regDate);
//	List<User> findUsersRegisteredAfter(LocalDate reg);
//	List<User> findUsersRegisteredAfter(LocalDate regDate);

	@Query("Select new code.jpa.customQueryJpql.UserSummaryDto(u.name, u.email) From User u")
	List<UserSummaryDto> findAllUsersSummary();

	@Query("Select new code.jpa.customQueryJpql.UserSummaryDto(u.name, u.email) From User u Where u.registrationDate >= :regDate")
	List<UserSummaryDto> findUsersSummaryRegisteredAfter(@Param(value = "regDate") LocalDate date);

	@Query("Select u From User u Where u.registrationDate >= :reg")
	Page<User> findUsersPageRegisteredAfterWithDynamicSort(
			@Param("reg") LocalDate date,
			Pageable pageable);  // Supports sorting & pagination

}
package lk.acpt.smartbizspring.repo;

import lk.acpt.smartbizspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM user u WHERE u.role = :role", nativeQuery = true)
    List<User> findByUserRole(@Param("role") String role);

}

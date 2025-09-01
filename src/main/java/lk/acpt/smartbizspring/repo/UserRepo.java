package lk.acpt.smartbizspring.repo;

import lk.acpt.smartbizspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

}

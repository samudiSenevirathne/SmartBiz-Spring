package lk.acpt.smartbizspring.repo;

import lk.acpt.smartbizspring.entity.Register;
import lk.acpt.smartbizspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegisterRepo extends JpaRepository<Register, Integer> {
    Optional<Register> findByUsername(String username);
    Optional<Register> findByUser(User savedUser);
    List<Register> findByUserIn(List<User> user);
    Optional<Register> findByPassword(String password);
}

package uz.script.wincrm.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.script.wincrm.utils.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByIdAndStatusNot(Long id, Status status);
    List<User> findAllByStatusNot(Status status);
    boolean existsByUsername(String username);
}

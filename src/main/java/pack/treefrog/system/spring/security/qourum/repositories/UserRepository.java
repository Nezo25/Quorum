package pack.treefrog.system.spring.security.qourum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pack.treefrog.system.spring.security.qourum.models.User;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository <User, UUID> {


Optional<User> findByUsername(String username);

}

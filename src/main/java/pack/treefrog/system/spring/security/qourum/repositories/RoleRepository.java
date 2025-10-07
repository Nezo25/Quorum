package pack.treefrog.system.spring.security.qourum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pack.treefrog.system.spring.security.qourum.models.Role;

public interface RoleRepository extends JpaRepository <Role, Long> {


Role findByName(String name);

}

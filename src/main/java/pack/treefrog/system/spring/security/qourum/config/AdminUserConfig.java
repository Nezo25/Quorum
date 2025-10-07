package pack.treefrog.system.spring.security.qourum.config;


import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder; // Usando a interface
import pack.treefrog.system.spring.security.qourum.models.Role;
import pack.treefrog.system.spring.security.qourum.models.User;
import pack.treefrog.system.spring.security.qourum.repositories.RoleRepository;
import pack.treefrog.system.spring.security.qourum.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  AdminUserConfig(RoleRepository roleRepository,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.roleRepository=roleRepository;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Variável declarada como 'roleAdmin'
        Optional<Role> roleAdmin = Optional.ofNullable(roleRepository.findByName(Role.Values.ADMIN.name()));
        var userAdmin = userRepository.findByUsername("admin");


        userAdmin.ifPresentOrElse(
                // CASO PRESENTE
                user -> {
                    System.out.println("admin ja existe");
                },

                // CASO AUSENTE (Cria o admin)
                () -> {
                    // Usando 'roleAdmin.ifPresent' no Optional<Role>
                    roleAdmin.ifPresent(role -> {
                        var user = new User();
                        user.setUsername("admin");

                        user.setPassword(passwordEncoder.encode("123"));


                        user.setRoles(Set.of(role));

                        userRepository.save(user);
                    });
                }
        );
    }
}

package pack.treefrog.system.spring.security.qourum.controller;


import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pack.treefrog.system.spring.security.qourum.dto.CreateUserDTO;
import pack.treefrog.system.spring.security.qourum.models.Role;
import pack.treefrog.system.spring.security.qourum.models.User;
import pack.treefrog.system.spring.security.qourum.repositories.RoleRepository;
import pack.treefrog.system.spring.security.qourum.repositories.UserRepository;

import java.util.Set;
import java.util.Optional;
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO dto) {

        // O findByName retorna um Optional<Role>
        Optional<Role> basicRoleOptional = Optional.ofNullable(roleRepository.findByName(Role.Values.BASIC.name()));

        // Verifica se a Role básica existe. Se não, lança uma exceção.
        if (basicRoleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role BASIC não encontrada.");
        }

        var userFromDb = userRepository.findByUsername(dto.username());

        if (userFromDb.isPresent()) {
            // 422 UNPROCESSABLE_ENTITY: indica que o servidor entendeu o conteúdo, mas não pode processá-lo.
            throw  new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário já existe.");
        }

        var user = new User();
        user.setUsername(dto.username());


        user.setPassword(this.passwordEncoder.encode(dto.password()));

        // Desempacota o Optional e usa o Role encontrado
        user.setRoles(Set.of(basicRoleOptional.get()));

        userRepository.save(user);

        // Retorna 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

package pack.treefrog.system.spring.security.qourum.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pack.treefrog.system.spring.security.qourum.dto.LoginRequest;
import pack.treefrog.system.spring.security.qourum.dto.LoginResponse;
import pack.treefrog.system.spring.security.qourum.repositories.UserRepository;

import java.time.Instant;


@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {


        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder; // Usando o BCrypt injetado
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username());

        // Garante que o usuário existe E que a senha está correta
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder) ) {
            // Mensagem de erro mais genérica é melhor para segurança
            throw new BadCredentialsException("Usuário ou senha inválidos!");
        }

        var now = Instant.now();
        // Token expira em 300 segundos (5 minutos)
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                // O subject ID (UUID) convertido para String
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();


        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));


    }
}

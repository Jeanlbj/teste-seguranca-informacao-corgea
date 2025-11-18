package com.exercicio.extra.controller;

import com.exercicio.extra.dto.LoginRequestDTO;
import com.exercicio.extra.dto.UsuarioCadastroDTO;
import com.exercicio.extra.entity.PerfilEnum;
import com.exercicio.extra.entity.Usuario;
import com.exercicio.extra.security.JwtUtils;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioCadastroDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setPerfil(PerfilEnum.APOSTADOR);

        Usuario savedUser = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.ok(Map.of("id", savedUser.getId(), "email", savedUser.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha())
            );

            UserDetails userDetails = usuarioService.buscarPorEmail(loginRequest.email())
                    .map(UserDetailsImpl::new)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtUtils.gerarToken(((UserDetailsImpl) userDetails).getUsuario());

            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }
}
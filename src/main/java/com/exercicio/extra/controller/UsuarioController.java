package com.exercicio.extra.controller;

import com.exercicio.extra.dto.LoginRequestDTO;
import com.exercicio.extra.dto.UsuarioResponseDTO;
import com.exercicio.extra.entity.Usuario;
import com.exercicio.extra.security.JwtUtils;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioService.autenticar(loginRequest.email(), loginRequest.senha());
        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        String token = jwtUtils.gerarToken(usuario);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> perfil(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Usuario usuario = userDetails.getUsuario();
        UsuarioResponseDTO dto = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
        return ResponseEntity.ok(dto);
    }
}

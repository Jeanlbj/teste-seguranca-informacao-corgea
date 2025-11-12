package com.exercicio.extra.service;

import com.exercicio.extra.entity.Carteira;
import com.exercicio.extra.entity.Usuario;
import com.exercicio.extra.repository.CarteiraRepository;
import com.exercicio.extra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarteiraRepository carteiraRepository; 

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuarioOpt = buscarPorEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return usuario;
            }
        }
        return null;
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario novoUsuario = usuarioRepository.save(usuario);

        Carteira novaCarteira = new Carteira();
        novaCarteira.setUsuario(novoUsuario);
        novaCarteira.setSaldo(0.0);
        carteiraRepository.save(novaCarteira);

        return novoUsuario;
    }
}
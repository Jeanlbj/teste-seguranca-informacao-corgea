package com.exercicio.extra.controller;

import com.exercicio.extra.dto.TransacaoDTO;
import com.exercicio.extra.entity.Carteira;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.CarteiraService;
import com.exercicio.extra.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final CarteiraService carteiraService;

    @GetMapping("/meu-extrato")
    public ResponseEntity<List<TransacaoDTO>> meuExtrato(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long usuarioId = userDetails.getUsuario().getId();
        Carteira carteira = carteiraService.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        List<TransacaoDTO> dtos = transacaoService.listarPorCarteira(carteira.getId()).stream()
                .map(t -> new TransacaoDTO(t.getId(), t.getTipo().name(), t.getValor(), t.getData()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
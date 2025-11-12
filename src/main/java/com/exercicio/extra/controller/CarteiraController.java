package com.exercicio.extra.controller;

import com.exercicio.extra.dto.CarteiraDTO;
import com.exercicio.extra.entity.Carteira;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.CarteiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carteira")
@RequiredArgsConstructor
public class CarteiraController {

    private final CarteiraService carteiraService;

    @GetMapping("/saldo")
    public ResponseEntity<CarteiraDTO> consultarSaldo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long usuarioId = userDetails.getUsuario().getId();
        Carteira carteira = carteiraService.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        return ResponseEntity.ok(new CarteiraDTO(carteira.getId(), usuarioId, carteira.getSaldo()));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<CarteiraDTO> adicionarSaldo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestParam Double valor) {
        Long usuarioId = userDetails.getUsuario().getId();
        Carteira carteira = carteiraService.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        carteiraService.adicionarSaldo(carteira, valor);
        return ResponseEntity.ok(new CarteiraDTO(carteira.getId(), usuarioId, carteira.getSaldo()));
    }
}

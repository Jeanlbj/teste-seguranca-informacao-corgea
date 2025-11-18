package com.exercicio.extra.controller;

import com.exercicio.extra.entity.Evento;
import com.exercicio.extra.service.ApostaService;
import com.exercicio.extra.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RelatorioController {

    private final EventoService eventoService;
    private final ApostaService apostaService;

    @GetMapping("/volume-apostas")
    public ResponseEntity<?> volumeApostasPorEvento() {
        List<Evento> eventos = eventoService.listarProximosEventos();

        var volumes = eventos.stream().collect(
                java.util.stream.Collectors.toMap(
                        Evento::getId,
                        evento -> apostaService.listarPorEventoId(evento.getId())
                                .stream()
                                .mapToDouble(aposta -> aposta.getValor())
                                .sum()
                )
        );
        return ResponseEntity.ok(volumes);
    }

    @GetMapping("/lucro-prejuizo")
    public ResponseEntity<?> lucroPrejuizo() {
        List<Evento> eventos = eventoService.listarProximosEventos();

        double totalApostado = eventos.stream()
                .flatMap(evento -> apostaService.listarPorEventoId(evento.getId()).stream())
                .mapToDouble(aposta -> aposta.getValor())
                .sum();

        double totalPago = eventos.stream()
                .flatMap(evento -> apostaService.listarPorEventoId(evento.getId()).stream())
                .mapToDouble(aposta -> aposta.getRetorno() != null ? aposta.getRetorno() : 0.0)
                .sum();

        double lucro = totalApostado - totalPago;

        return ResponseEntity.ok(Map.of("lucroPrejuizo", lucro));
    }
}

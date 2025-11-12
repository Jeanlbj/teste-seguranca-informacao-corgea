package com.exercicio.extra.controller;

import com.exercicio.extra.dto.EventoDTO;
import com.exercicio.extra.dto.EventoRequestDTO;
import com.exercicio.extra.entity.Esporte;
import com.exercicio.extra.entity.Evento;
import com.exercicio.extra.entity.Time;
import com.exercicio.extra.security.UserDetailsImpl;
import com.exercicio.extra.service.EsporteService;
import com.exercicio.extra.service.EventoService;
import com.exercicio.extra.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final EsporteService esporteService;
    private final TimeService timeService;

    @GetMapping("/publicos")
    public ResponseEntity<List<EventoDTO>> listarPublicos() {
        List<Evento> eventos = eventoService.listarProximosEventos();
        List<EventoDTO> dtos = eventos.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoRequestDTO dto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Evento evento = fromDTO(dto);
        Evento salvo = eventoService.salvar(evento);
        return ResponseEntity.ok(toDTO(salvo));
    }

    private Evento fromDTO(EventoRequestDTO dto) {
        Evento evento = new Evento();

        Esporte esporte = esporteService.buscarPorId(dto.esporteId())
                .orElseThrow(() -> new RuntimeException("Esporte não encontrado"));

        Time timeA = timeService.buscarPorId(dto.timeAId())
                .orElseThrow(() -> new RuntimeException("Time A não encontrado"));

        Time timeB = timeService.buscarPorId(dto.timeBId())
                .orElseThrow(() -> new RuntimeException("Time B não encontrado"));

        evento.setEsporte(esporte);
        evento.setTimeA(timeA);
        evento.setTimeB(timeB);
        evento.setDataHora(dto.dataHora());
        evento.setLocal(dto.local());
        evento.setOddTimeA(dto.oddTimeA());
        evento.setOddTimeB(dto.oddTimeB());
        evento.setOddEmpate(dto.oddEmpate());

        return evento;
    }

    private EventoDTO toDTO(Evento evento) {
        return new EventoDTO(
                evento.getId(),
                evento.getEsporte().getId(),
                evento.getTimeA().getId(),
                evento.getTimeB().getId(),
                evento.getDataHora(),
                evento.getLocal(),
                evento.getOddTimeA(),
                evento.getOddTimeB(),
                evento.getOddEmpate(),
                evento.getResultado() != null ? evento.getResultado().name() : null
        );
    }
}
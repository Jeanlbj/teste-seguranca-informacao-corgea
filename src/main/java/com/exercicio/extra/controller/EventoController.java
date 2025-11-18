package com.exercicio.extra.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.time.LocalDateTime;
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
private static final Logger logger = LoggerFactory.getLogger(EventoController.class);

    @GetMapping("/publicos")
    public ResponseEntity<List<EventoDTO>> listarPublicos() {
        List<Evento> eventos = eventoService.listarProximosEventos();
        List<EventoDTO> dtos = eventos.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EVENTO_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<EventoDTO> criarEvento(@RequestBody @Valid EventoRequestDTO dto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateEventoRequest(dto);
        Evento evento = fromDTO(dto);
        Evento salvo = eventoService.salvar(evento);
        return ResponseEntity.ok(toDTO(salvo));
    }

    private Evento fromDTO(EventoRequestDTO dto) {
        Evento evento = new Evento();

        Esporte esporte = esporteService.buscarPorId(dto.esporteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esporte não encontrado"));

        Time timeA = timeService.buscarPorId(dto.timeAId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time A não encontrado"));

        Time timeB = timeService.buscarPorId(dto.timeBId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time B não encontrado"));

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
private void validateEventoRequest(EventoRequestDTO dto) {
    if (dto == null) {
        logger.warn("Tentativa de criação de evento com payload nulo");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisição inválida");
    }
    if (dto.esporteId() == null || dto.timeAId() == null || dto.timeBId() == null) {
        logger.warn("Tentativa de criação de evento com IDs obrigatórios ausentes");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IDs de esporte e times são obrigatórios");
    }
    if (dto.timeAId().equals(dto.timeBId())) {
        logger.warn("Tentativa de criação de evento com times iguais: {}", dto.timeAId());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Times A e B devem ser diferentes");
    }
    if (dto.local() == null || dto.local().trim().isEmpty() || dto.local().length() > 255) {
        logger.warn("Tentativa de criação de evento com local inválido");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Local inválido");
    }
    if (dto.oddTimeA() == null || dto.oddTimeA() <= 1.0
            || dto.oddTimeB() == null || dto.oddTimeB() <= 1.0
            || dto.oddEmpate() == null || dto.oddEmpate() <= 1.0) {
        logger.warn("Tentativa de criação de evento com odds inválidas");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Odds devem ser maiores que 1.0");
    }
    if (dto.dataHora() == null) {
        logger.warn("Tentativa de criação de evento com data/hora ausente");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora são obrigatórias");
    }
    try {
        if (dto.dataHora().isBefore(LocalDateTime.now())) {
            logger.warn("Tentativa de criação de evento com data/hora no passado");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data/hora deve ser no futuro");
        }
    } catch (Exception e) {
        logger.warn("Formato de data/hora inválido ao criar evento");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de data/hora inválido");
    }
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
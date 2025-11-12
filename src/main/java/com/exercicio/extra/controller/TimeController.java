package com.exercicio.extra.controller;

import com.exercicio.extra.dto.TimeDTO;
import com.exercicio.extra.dto.TimeRequestDTO;
import com.exercicio.extra.entity.Esporte;
import com.exercicio.extra.entity.Time;
import com.exercicio.extra.service.EsporteService;
import com.exercicio.extra.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;
    private final EsporteService esporteService;

    @GetMapping
    public ResponseEntity<List<TimeDTO>> listarTodos() {
        List<TimeDTO> dtos = timeService.listarTodos()
                .stream()
                .map(t -> new TimeDTO(t.getId(), t.getEsporte().getId(), t.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TimeDTO> criar(@RequestBody TimeRequestDTO dto) {
        Esporte esporte = esporteService.buscarPorId(dto.esporteId())
                .orElseThrow(() -> new RuntimeException("Esporte não encontrado"));

        Time time = new Time();
        time.setNome(dto.nome());
        time.setEsporte(esporte);

        Time salvo = timeService.salvar(time);
        return ResponseEntity.ok(new TimeDTO(salvo.getId(), salvo.getEsporte().getId(), salvo.getNome()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TimeDTO> atualizar(@PathVariable Long id, @RequestBody TimeRequestDTO dto) {
        Time time = timeService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Time não encontrado"));

        Esporte esporte = esporteService.buscarPorId(dto.esporteId())
                .orElseThrow(() -> new RuntimeException("Esporte não encontrado"));

        time.setNome(dto.nome());
        time.setEsporte(esporte);

        Time atualizado = timeService.salvar(time);
        return ResponseEntity.ok(new TimeDTO(atualizado.getId(), atualizado.getEsporte().getId(), atualizado.getNome()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        timeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
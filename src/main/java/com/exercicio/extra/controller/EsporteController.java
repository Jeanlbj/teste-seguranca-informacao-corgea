package com.exercicio.extra.controller;

import com.exercicio.extra.dto.EsporteDTO;
import com.exercicio.extra.dto.EsporteRequestDTO;
import com.exercicio.extra.entity.Esporte;
import com.exercicio.extra.service.EsporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/esportes")
@RequiredArgsConstructor
public class EsporteController {

    private final EsporteService esporteService;

    @GetMapping
    public ResponseEntity<List<EsporteDTO>> listarTodos() {
        List<EsporteDTO> dtos = esporteService.listarTodos()
                .stream()
                .map(e -> new EsporteDTO(e.getId(), e.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EsporteDTO> criar(@RequestBody EsporteRequestDTO dto) {
        Esporte esporte = new Esporte();
        esporte.setNome(dto.nome());
        Esporte salvo = esporteService.salvar(esporte);
        return ResponseEntity.ok(new EsporteDTO(salvo.getId(), salvo.getNome()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EsporteDTO> atualizar(@PathVariable Long id, @RequestBody EsporteRequestDTO dto) {
        Esporte esporte = esporteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Esporte não encontrado"));
        esporte.setNome(dto.nome());
        Esporte atualizado = esporteService.salvar(esporte);
        return ResponseEntity.ok(new EsporteDTO(atualizado.getId(), atualizado.getNome()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        esporteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
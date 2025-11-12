package com.exercicio.extra.controller;

import com.exercicio.extra.dto.PromocaoDTO;
import com.exercicio.extra.dto.PromocaoRequestDTO;
import com.exercicio.extra.entity.Promocao;
import com.exercicio.extra.service.PromocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promocoes")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoService promocaoService;

    @GetMapping
    public ResponseEntity<List<PromocaoDTO>> listarPromocoes() {
        List<PromocaoDTO> dtos = promocaoService.listarPromocoesValidas().stream()
                .map(p -> new PromocaoDTO(p.getId(), p.getTitulo(), p.getDescricao(), p.getValorBonus(), p.getDataValidade()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PromocaoDTO> criarPromocao(@RequestBody PromocaoRequestDTO dto) {
        Promocao promocao = new Promocao();
        promocao.setTitulo(dto.titulo());
        promocao.setDescricao(dto.descricao());
        promocao.setValorBonus(dto.valorBonus());
        promocao.setDataValidade(dto.dataValidade());

        Promocao salva = promocaoService.salvar(promocao);

        return ResponseEntity.ok(new PromocaoDTO(salva.getId(), salva.getTitulo(), salva.getDescricao(), salva.getValorBonus(), salva.getDataValidade()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        promocaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
package com.uea.aps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uea.aps.dto.AtividadeDetalhadaDTO;
import com.uea.aps.model.Atividade;
import com.uea.aps.service.AtividadeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/Atividades")
public class AtividadeController {
    @Autowired
    private AtividadeService atividadeService;

    // Listar todos os atividades
    @GetMapping
    public ResponseEntity<List<Atividade>> listar() {
        List<Atividade> atividades = atividadeService.listar();
        if (atividades.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a lista vazia
        }
        return ResponseEntity.ok(atividades); // Retorna 200 OK
    }

    // Buscar atividade por ID
    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return atividadeService.buscarPorId(id)
                .map(ResponseEntity::ok) // Retorna 200 OK se encontrado
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna 404 NOT FOUND se não encontrado
    }

    // Criar um nova ativiadde
    @PostMapping
    @PreAuthorize("hasrole('ADMIN')")
    public ResponseEntity<Atividade> criar(@RequestBody Atividade atividade) {
        Atividade novAtividade = atividadeService.salvarAtividade(atividade);
        return ResponseEntity.status(HttpStatus.CREATED).body(novAtividade); // Retorna 201 Created
    }

    // Atualizar um atividade existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARIO')")
    public ResponseEntity<Atividade> atualizar(@PathVariable Long id, @RequestBody Atividade atividade) {
        if (!atividadeService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se o atividade não existir
        }
        atividade.setId(id);
        Atividade atividadeAtualizado = atividadeService.salvarAtividade(atividade);
        return ResponseEntity.ok(atividadeAtualizado); // Retorna 200 OK se atualizado com sucesso
    }

    // Deletar uma atividade existente
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        atividadeService.deletarAtividade(id);
    }

    @GetMapping("/detalhado/{id}")
    public ResponseEntity<AtividadeDetalhadaDTO> buscarAtividade(@PathVariable Long id) {
        return atividadeService.buscarPorId(id)
                .map(atividade -> {
                    AtividadeDetalhadaDTO atividadeDetalhadaDTO = new AtividadeDetalhadaDTO();
                    atividadeDetalhadaDTO.setId(atividade.getId());
                    atividadeDetalhadaDTO.setNome(atividade.getNome());
                    atividadeDetalhadaDTO.setObjetivo(atividade.getObjetivo());
                    atividadeDetalhadaDTO.setCategoria(atividade.getCategoriaPrincipal().getNome());
                    atividadeDetalhadaDTO.setCurso(atividade.getCurso().getNome());
                    return ResponseEntity.ok(atividadeDetalhadaDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}

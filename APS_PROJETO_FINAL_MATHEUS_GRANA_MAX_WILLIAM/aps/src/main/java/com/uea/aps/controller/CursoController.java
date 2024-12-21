package com.uea.aps.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uea.aps.dto.CursoDetalhadoDTO;
import com.uea.aps.model.Atividade;
import com.uea.aps.model.Curso;
import com.uea.aps.service.CursoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;

    // Listar todos os cursos
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        List<Curso> cursos = cursoService.listar();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a lista estiver vazia
        }
        return ResponseEntity.ok(cursos); // Retorna 200 OK
    }

    // Buscar curso por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok) // Retorna 200 OK se encontrado
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna 404 Not Found
    }

    // Criar um novo curso
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Curso> criar(@RequestBody Curso curso) {
        Curso novoCurso = cursoService.salvar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso); // Retorna 201 Created
    }

    // Atualizar um curso existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso) {
        if (!cursoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se o curso não existir
        }
        curso.setId(id);
        Curso cursoAtualizado = cursoService.salvar(curso);
        return ResponseEntity.ok(cursoAtualizado); // Retorna 200 OK se atualizado com sucesso
    }

    // Deletar um curso por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!cursoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se o curso não existir
        }
        cursoService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content se deletado com sucesso
    }

    @GetMapping("/detalhado/{id}")
    public ResponseEntity<CursoDetalhadoDTO> buscarCursoPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id)
                .map(curso -> {
                    CursoDetalhadoDTO cursoDTO = new CursoDetalhadoDTO();
                    cursoDTO.setId(curso.getId());
                    cursoDTO.setNome(curso.getNome());
                    cursoDTO.setAtividades(curso.getAtividades().stream()
                            .map(Atividade::getNome)
                            .collect(Collectors.toList()));
                    return ResponseEntity.ok(cursoDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}

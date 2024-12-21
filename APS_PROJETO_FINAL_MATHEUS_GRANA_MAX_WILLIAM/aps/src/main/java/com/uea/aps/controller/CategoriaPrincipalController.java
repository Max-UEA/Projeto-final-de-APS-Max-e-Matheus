package com.uea.aps.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uea.aps.dto.CategoriaPrincipalDTO;
import com.uea.aps.model.CategoriaPrincipal;
import com.uea.aps.service.CategoriaPrincipalService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/categorias")
public class CategoriaPrincipalController {
    @Autowired
    private CategoriaPrincipalService categoriaPrincipalService;

    // Listar todas as categorias
    @GetMapping
    public ResponseEntity<List<CategoriaPrincipal>> listar() {
        List<CategoriaPrincipal> categorias = categoriaPrincipalService.listar();
        return ResponseEntity.ok(categorias); // Retorna 200 OK
    }

    // Buscar categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPrincipal> buscarPorId(@PathVariable Long id) {
        return categoriaPrincipalService.buscarPorId(id)
                .map(ResponseEntity::ok) // Retorna 200 OK se encontrado
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna 404 Not Found se não encontrado
    }

    // Criar uma nova categoria
    @PostMapping
    public ResponseEntity<CategoriaPrincipal> criar(@RequestBody CategoriaPrincipal categoriaPrincipal) {
        CategoriaPrincipal novaCategoria = categoriaPrincipalService.salvar(categoriaPrincipal);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria); // Retorna 201 Created
    }

    // Atualizar uma categoria existente
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaPrincipal> atualizar(@PathVariable Long id, @RequestBody CategoriaPrincipal categoriaPrincipal) {
        if (!categoriaPrincipalService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se a categoria não
                                                                        // existir
        }
        categoriaPrincipal.setId(id);
        CategoriaPrincipal categoriaAtualizada = categoriaPrincipalService.salvar(categoriaPrincipal);
        return ResponseEntity.ok(categoriaAtualizada); // Retorna 200 OK se atualizado com sucesso
    }

    // Deletar uma categoria por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!categoriaPrincipalService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se a categoria não
                                                                        // existir
        }
        categoriaPrincipalService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content se deletado com sucesso
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<CategoriaPrincipalDTO>> listarCategorias() {
        List<CategoriaPrincipalDTO> categoriasDTO = categoriaPrincipalService.listar().stream()
                .map(categoria -> {
                    CategoriaPrincipalDTO dto = new CategoriaPrincipalDTO();
                    dto.setId(categoria.getId());
                    dto.setNome(categoria.getNome());
                    dto.setQuantidadeAtividades(categoria.getAtividades().size());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoriasDTO);
    }
}

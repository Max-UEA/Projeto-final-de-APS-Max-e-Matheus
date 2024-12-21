package com.uea.aps.controller;

import java.util.List;

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

import com.uea.aps.dto.FotoDetalhadaDTO;
import com.uea.aps.model.Foto;
import com.uea.aps.service.FotoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/fotos")
public class FotoController {
    @Autowired
    private FotoService fotoService;

    // Listar todos as fotos
    @GetMapping
    public ResponseEntity<List<Foto>> listar() {
        List<Foto> fotos = fotoService.listar();
        return ResponseEntity.ok(fotos); // Retorna 200 OK
    }

    // Buscar foto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Foto> buscarPorId(@PathVariable Long id) {
        return fotoService.buscarPorId(id)
                .map(ResponseEntity::ok) // Retorna 200 OK se encontrado
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna 404 Not Found se não encontrado
    }

    // Criar uma nova foto
    @PostMapping
    public ResponseEntity<Foto> criar(@RequestBody Foto foto) {
        Foto novaFoto = fotoService.salvar(foto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaFoto); // Retorna 201 Created
    }
    
    // Atualizar uma foto existente
    @PutMapping("/{id}")
    public ResponseEntity<Foto> atualizar(@PathVariable Long id, @RequestBody Foto foto) {
        if (!fotoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se o livro não existir
        }
        foto.setId(id);
        Foto fotoatualizada = fotoService.salvar(foto);
        return ResponseEntity.ok(fotoatualizada); // Retorna 200 OK se atualizado com sucesso
    }

    // Deletar uma foto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!fotoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 Not Found se o livro não existir
        }
        fotoService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content se deletado com sucesso
    }

    @GetMapping("/resumo/{id}")
    public ResponseEntity<FotoDetalhadaDTO> buscarLivro(@PathVariable Long id) {
        return fotoService.buscarPorId(id)
                .map(foto -> {
                    FotoDetalhadaDTO fotoDTO = new FotoDetalhadaDTO();
                    fotoDTO.setId(foto.getId());
                    fotoDTO.setDescricao(foto.getDescricao());
                    fotoDTO.setAtividade(foto.getAtividade().getNome());
                    return ResponseEntity.ok(fotoDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.uea.aps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uea.aps.model.CategoriaPrincipal;
import com.uea.aps.repository.CategoriaPrincipalRepository;

@Service
public class CategoriaPrincipalService {
    @Autowired
    private CategoriaPrincipalRepository categoriaPrincipalRepository;

    public List<CategoriaPrincipal> listar() {
        return categoriaPrincipalRepository.findAll();
    }

    public Optional<CategoriaPrincipal> buscarPorId(Long id) {
        return categoriaPrincipalRepository.findById(id);
    }

    public CategoriaPrincipal salvar(CategoriaPrincipal categoriaPrincipal) {
        return categoriaPrincipalRepository.save(categoriaPrincipal);
    }

    public void deletar(Long id) {
        categoriaPrincipalRepository.deleteById(id);
    }
}

package com.uea.aps.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uea.aps.model.Foto;
import com.uea.aps.repository.FotoRepository;

@Service
public class FotoService {
    @Autowired
    private FotoRepository fotoRepository;

    public List<Foto> listar() {
        return fotoRepository.findAll();
    }

    public Optional<Foto> buscarPorId(Long id) {
        return fotoRepository.findById(id);
    }

    public Foto salvar(Foto foto) {
        return fotoRepository.save(foto);
    }

    public void deletar(Long id) {
        fotoRepository.deleteById(id);
    }
}

package com.uea.aps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uea.aps.model.Curso;

public interface CursoRepository extends JpaRepository <Curso, Long> {

}

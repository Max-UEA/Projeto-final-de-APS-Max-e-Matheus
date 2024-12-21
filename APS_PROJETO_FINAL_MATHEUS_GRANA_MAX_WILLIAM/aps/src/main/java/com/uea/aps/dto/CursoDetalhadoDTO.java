package com.uea.aps.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CursoDetalhadoDTO {
    private Long id;
    private String nome;
    private List<String> atividades;
}

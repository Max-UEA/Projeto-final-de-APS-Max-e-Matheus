package com.uea.aps.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtividadeDetalhadaDTO {
    private Long id;
    private String nome;
    private String objetivo;
    private Boolean publicado;
    private String publicoAlvo;
    private LocalDate data;
    private String curso;
    private String foto;
    private String categoria;
}

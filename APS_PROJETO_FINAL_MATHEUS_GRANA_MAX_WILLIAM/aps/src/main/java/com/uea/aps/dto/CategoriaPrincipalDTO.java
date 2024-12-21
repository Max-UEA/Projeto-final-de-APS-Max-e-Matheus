package com.uea.aps.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaPrincipalDTO {
    private Long id;
    private String nome;
    private int quantidadeAtividades;
}

package com.schools.logesco.common;

import com.schools.logesco.classe.dto.ClasseDto;
import com.schools.logesco.classe.entity.Classe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClasseMapper {
    public static ClasseDto toDto(Classe c) {
        return ClasseDto.builder()
                .id(c.getId())
                .nom(c.getNom())
                .niveau(c.getNiveau())
                .professeurPrincipal(c.getProfesseurPrincipal())
                .build();
    }
}

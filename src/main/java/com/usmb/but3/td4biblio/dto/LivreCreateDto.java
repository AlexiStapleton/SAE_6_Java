package com.usmb.but3.td4biblio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LivreCreateDto extends DocumentCreateDto {
    private Integer nbPages;
    private String codeIsbn;
}

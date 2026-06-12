package com.usmb.but3.td4biblio.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DvdDetailResponseDto extends DocumentDetailResponseDto {
    private Integer duree;
}

package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.DvdCreateDto;
import com.usmb.but3.td4biblio.dto.DvdDetailResponseDto;
import com.usmb.but3.td4biblio.dto.DvdResponseDto;
import com.usmb.but3.td4biblio.entity.Dvd;
import com.usmb.but3.td4biblio.mapper.DvdMapper;
import com.usmb.but3.td4biblio.mapper.LivreMapper;
import com.usmb.but3.td4biblio.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DvdService
    extends AbstractDocumentService<Dvd, DvdResponseDto, DvdDetailResponseDto, DvdCreateDto> {

    public DvdService(DvdRepo repository,
                        DvdMapper mapper,
                        AuteurRepo auteurRepository,
                        EditeurRepo editeurRepository,
                        BibliothequeRepo bibliothequeRepository,
                        GenreDocumentRepo genreRepository,
                        CodeRaisonRepo codeRaisonRepository) {
        super(repository,
                mapper,
                auteurRepository,
                editeurRepository,
                bibliothequeRepository,
                genreRepository,
                codeRaisonRepository);
    }

}

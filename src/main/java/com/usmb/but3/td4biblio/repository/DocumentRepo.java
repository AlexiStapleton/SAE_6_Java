package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepo extends JpaRepository<Document, Integer> {

    List<Document> findByTitreContainingIgnoreCase(String titre);

    List<Document> findByTitreStartsWithIgnoreCase(String titre);

    List<Document> findByTitreIgnoreCase(String titre);

    List<Document> findByAuteurId(Integer auteurId);

    List<Document> findByAuteurId(Integer auteurId, Sort sort);

    List<Document> findAllByOrderByTitreAsc();
}
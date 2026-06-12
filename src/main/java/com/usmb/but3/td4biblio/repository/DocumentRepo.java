package com.usmb.but3.td4biblio.repository;

import com.usmb.but3.td4biblio.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Integer> {
}

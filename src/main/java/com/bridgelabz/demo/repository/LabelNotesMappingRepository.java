package com.bridgelabz.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.demo.model.LabelNotesMapping;

@Repository
public interface LabelNotesMappingRepository extends JpaRepository<LabelNotesMapping, Long>{

}

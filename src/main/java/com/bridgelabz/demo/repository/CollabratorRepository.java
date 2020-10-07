package com.bridgelabz.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.demo.model.Collabrator;

@Repository
public interface CollabratorRepository extends JpaRepository<Collabrator, Long> {

}

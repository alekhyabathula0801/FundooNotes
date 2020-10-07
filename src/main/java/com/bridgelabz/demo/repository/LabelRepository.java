package com.bridgelabz.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.demo.model.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long>{

}

package com.bridgelabz.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bridgelabz.demo.model.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

	@Query(value = "select * from label where user_id = :userId", nativeQuery = true)
	List<Label> findAllLabelsByUserId(@Param("userId") Long userId);

}

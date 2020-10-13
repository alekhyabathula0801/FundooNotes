package com.bridgelabz.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bridgelabz.demo.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	@Query(value = "select * from fundoo_notes where user_id = :userId AND is_archive = :archive and is_trash = :trash", nativeQuery = true)
	List<Note> findAllNotesByUserId(@Param("userId") Long userId, @Param("archive") Boolean is_archive,
			@Param("trash") Boolean is_trash);

}

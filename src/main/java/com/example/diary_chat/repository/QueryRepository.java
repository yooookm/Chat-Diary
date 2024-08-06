package com.example.diary_chat.repository;

import com.example.diary_chat.domain.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends JpaRepository<Ask, Long> {

    @Query(value = "SELECT * FROM Ask WHERE status = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Ask findRandomQuery();
}
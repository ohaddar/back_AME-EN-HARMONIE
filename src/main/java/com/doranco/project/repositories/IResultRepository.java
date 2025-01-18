package com.doranco.project.repositories;

import com.doranco.project.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IResultRepository extends JpaRepository<Result,Long> {
    List<Result> getResultsByUserId(Long userId);
}

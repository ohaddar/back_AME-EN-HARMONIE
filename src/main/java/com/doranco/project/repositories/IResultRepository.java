package com.doranco.project.repositories;

import com.doranco.project.entities.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResultRepository extends MongoRepository<Result,String> {
}

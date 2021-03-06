package com.example.miocarditepediatrica.repository;

import com.example.miocarditepediatrica.domain.user.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
}

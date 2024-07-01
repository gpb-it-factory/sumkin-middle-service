package com.gpb.sumkin_middle_service.repositories;

import com.gpb.sumkin_middle_service.entities.MyError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MyErrorRepository extends JpaRepository<MyError, UUID> {
}

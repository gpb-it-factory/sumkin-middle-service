package com.gpb.sumkin_middle_service.repositories;

import com.gpb.sumkin_middle_service.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID>,
        PagingAndSortingRepository<Transfer, UUID> {
}

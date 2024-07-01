package com.gpb.sumkin_middle_service.repositories;

import com.gpb.sumkin_middle_service.entities.AccountGpb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AccountGpbRepository extends JpaRepository<AccountGpb, UUID>,
        PagingAndSortingRepository<AccountGpb, UUID> {
}

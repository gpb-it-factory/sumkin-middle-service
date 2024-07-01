package com.gpb.sumkin_middle_service.repositories;

import com.gpb.sumkin_middle_service.entities.UserGpb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserGpbRepository extends PagingAndSortingRepository<UserGpb, UUID>,
        JpaRepository<UserGpb, UUID> {

    Optional<UserGpb> getByTgId(Long tgId);
}

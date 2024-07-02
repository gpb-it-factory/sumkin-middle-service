package com.gpb.sumkin_middle_service.repositories;

import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountGpbRepository extends JpaRepository<AccountGpb, UUID> {


//    @Query(value = """
//            SELECT new AccountDto(a.id, a.accountName, a.amount)
//            FROM AccountGpb a, UserGpb u
//            WHERE u.tgId = :tgId """)
//    List<AccountDto> findByTgId(@Param("tgId") Long tgId);

    @Query(value = """ 
            SELECT new AccountDto(a.id, a.accountName, a.amount)
            FROM AccountGpb a, UserGpb u
            WHERE u.tgUsername = :tgUsername""")
    List<AccountDto> findByTgUsername(@Param("tgUsername") String tgUsername);

    @Query(value = """ 
            SELECT new AccountGpb (a.id, a.userId, u.tgId, a.amount, a.accountName)
            FROM AccountGpb a, UserGpb u
            WHERE a.userId = u.id
            AND u.tgUsername = :tgUsername""")
    AccountGpb findByUsername(@Param("tgUsername") String tgUsername);

    Optional<AccountGpb> findByTgId(Long tgId);
}

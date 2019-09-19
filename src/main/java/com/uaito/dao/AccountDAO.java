package com.uaito.dao;

import com.uaito.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AccountDAO extends JpaRepository<Account, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE BankslipRecords SET Status = ?1 WHERE Tracking_ID = ?2 AND Org_ID = ?3", nativeQuery = true)
    int updateAccount(String status, String trackingID, String orgId);
}

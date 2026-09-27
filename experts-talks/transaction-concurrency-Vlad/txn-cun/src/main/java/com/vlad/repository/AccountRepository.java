package com.vlad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vlad.model.Account;

import jakarta.persistence.LockModeType;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    @Query("SELECT a.balance FROM Account a WHERE a.iban = :iban")
    Optional<Long> findBalanceByIban(@Param("iban") String iban);
    
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.iban = :iban")
    void updateBalance(@Param("iban") String iban, @Param("amount") Long amount);
    
    Optional<Account> findByIban(String iban);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.iban = :iban")
    Optional<Account> findByIbanForUpdate(@Param("iban") String iban);
}
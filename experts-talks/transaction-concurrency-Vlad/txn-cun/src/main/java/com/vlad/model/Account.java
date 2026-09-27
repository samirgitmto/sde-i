package com.vlad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "iban", length = 34)
    private String iban;
    
    @Column(name = "balance")
    private Long balance;
    
    // Constructors, getters, setters
    public Account() {}
    
    public Account(String iban, Long balance) {
        this.iban = iban;
        this.balance = balance;
    }
    
    // Getters and setters
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }
}
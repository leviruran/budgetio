package com.wallet_web_app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet_web_app.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}

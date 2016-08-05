package net.mybluemix.asmilk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.mybluemix.asmilk.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}

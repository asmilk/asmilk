package net.mybluemix.asmilk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.mybluemix.asmilk.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a where a.id = (select max(b.id) from Account b)")
	Account findMax();

}

package net.mybluemix.asmilk.service;

import net.mybluemix.asmilk.domain.Account;

public interface AccountService {

	Account save(Account account);

	Account findOne(Long id);
	
	Account findMax();

}

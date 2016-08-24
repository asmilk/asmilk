package net.mybluemix.asmilk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.repository.AccountRepository;
import net.mybluemix.asmilk.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public Account save(Account account) {
		return this.getAccountRepository().save(account);
	}

	@Override
	@Transactional(readOnly = true)
	public Account findOne(Long id) {
		return this.getAccountRepository().findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Account findMax() {
		return this.getAccountRepository().findMax();
	}

}

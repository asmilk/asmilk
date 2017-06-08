package net.mybluemix.asmilk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.mybluemix.asmilk.domain.Account;
import net.mybluemix.asmilk.repository.AccountRepository;
import net.mybluemix.asmilk.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account save(Account account) {
		return this.accountRepository.save(account);
	}
	
	@Override
	public void delete(Long id) {
		this.accountRepository.delete(id);
	}

	@Override
	public Account findOne(Long id) {
		return this.accountRepository.findOne(id);
	}

	@Override
	public Account findMax() {
		return this.accountRepository.findMax();
	}

	@Override
	public Page<Account> findAll(Pageable pageable) {
		return this.accountRepository.findAll(pageable);
	}
	
	@Override
	public Page<Account> findAll(Example<Account> example, Pageable pageable) {
		return this.accountRepository.findAll(example, pageable);
	}

	@Override
	public void sysGC() {
		this.accountRepository.sysGC();
	}

}

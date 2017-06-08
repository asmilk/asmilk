package net.mybluemix.asmilk.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import net.mybluemix.asmilk.domain.Account;

@Validated
@Transactional
public interface AccountService {

	@NotNull
	Account save(@NotNull Account account);
	
	void delete(@Min(1) Long id);

	@Transactional(readOnly = true)
	@NotNull(message = "{account.find.null}")
	Account findOne(@Min(1) Long id);

	@PreAuthorize("hasRole('USER')")
	@Transactional(readOnly = true)
	@NotNull(message = "{account.any.null}")
	Account findMax();

	@Transactional(readOnly = true)
	@NotNull
	Page<Account> findAll(Pageable pageable);
	
	@Transactional(readOnly = true)
	@NotNull
	Page<Account> findAll(Example<Account> example, Pageable pageable);

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(readOnly = true)
	void sysGC();

}

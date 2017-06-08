package net.mybluemix.asmilk.domain;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.google.api.client.util.Key;

import net.mybluemix.asmilk.auditing.AbstractAuditingEntity;

@Entity
@Cacheable
@Cache(usage = NONSTRICT_READ_WRITE)
public class Account extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 2353899894673301524L;

	@Key
	@Length(min = 5, max = 10)
	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return super.toString() + "; Account [name=" + name + "]";
	}

}

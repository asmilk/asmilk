package net.mybluemix.asmilk.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.api.client.util.Key;

@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = -8992119899897772408L;

	@Id
	@Key
	@GeneratedValue(strategy = IDENTITY)
	private long id;

	@Key
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + "]";
	}

}

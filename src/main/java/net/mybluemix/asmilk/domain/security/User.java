package net.mybluemix.asmilk.domain.security;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.api.client.util.Key;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 6136026319634997337L;

	@Id
	@Key
	private String username;

	@Key
	private String password;

	@Key
	private Boolean enabled;

	@OneToMany(mappedBy = "user")
	private Set<Authoritie> Authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Authoritie> getAuthorities() {
		return Authorities;
	}

	public void setAuthorities(Set<Authoritie> authorities) {
		Authorities = authorities;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", enabled=" + enabled + ", Authorities="
				+ Authorities + "]";
	}

}

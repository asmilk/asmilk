package net.mybluemix.asmilk.auditing;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.google.api.client.util.Key;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {

	@Key
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Key
	@Version
	private Long version;

	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	@LastModifiedBy
	@Column(name = "LAST_MODIFIED_BY")
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "AbstractAuditingEntity [id=" + id + ", version=" + version + "]";
	}

}

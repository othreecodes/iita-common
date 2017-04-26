/**
 * 
 */
package org.iita.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mobreza
 * 
 */
@Entity
@Table(name = "UserSupervisor")
public class UserSupervisor {
	private Long id;
	private String application;
	private User user;
	private User supervisor;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the application
	 */
	@Column(length = 100)
	public String getApplication() {
		return this.application;
	}

	/**
	 * @return the user
	 */
	@ManyToOne(cascade = {})
	@JoinColumn(name = "userId")
	public User getUser() {
		return this.user;
	}

	/**
	 * @return the supervisor
	 */
	@ManyToOne(cascade = {})
	@JoinColumn(name = "supervisorId")
	public User getSupervisor() {
		return this.supervisor;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}
}

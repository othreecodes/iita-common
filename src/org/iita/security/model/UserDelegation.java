/**
 * 
 */
package org.iita.security.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * @author mobreza, KOraegbunam
 * 
 */
@Entity
@Table(name = "UserDelegation")
public class UserDelegation {
	private Long id;
	private User owner;
	private User delegatedTo;
	private String application;
	private Date fromDate = null;
	private Date toDate = null;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the owner
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "ownerId")
	public User getOwner() {
		return this.owner;
	}

	/**
	 * @return the delegatedTo
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "delegatedTo")
	public User getDelegatedTo() {
		return this.delegatedTo;
	}

	/**
	 * @return the application
	 */
	@Column(length = 50, nullable = false)
	public String getApplication() {
		return this.application;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @param delegatedTo the delegatedTo to set
	 */
	public void setDelegatedTo(User delegatedTo) {
		this.delegatedTo = delegatedTo;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	
	@Column(nullable = false)
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(nullable = false)
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * Get duration of delegation
	 */
	@Transient
	public boolean getTimeElapsed() {
		if (toDate != null) {
			Date date = new Date();
			Period period = new Period(date.getTime(), toDate.getTime(), PeriodType.minutes());
			int mins = period.getMinutes();
			System.out.println("TIME ELAPSED IN MINUTES: " + mins);
			if (mins > 0)
				return false;
			else 
				return true;
			
		} else
			return false;
	}
}

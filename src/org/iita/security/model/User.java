package org.iita.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.CollectionOfElements;
import org.iita.security.UserAccess;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

/**
 * All user related information. This object can be persisted in a Session.
 * 
 * @author aafolayan
 * @author mobreza
 */
@SuppressWarnings("unchecked")
@Entity
public class User implements org.springframework.security.userdetails.UserDetails, Comparable<User>, UserAccess, Serializable {
	private static final GrantedAuthority[] GRANTED_AUTHORITY_TYPE = new GrantedAuthority[] {};

	private static final Log LOG = LogFactory.getLog(User.class);

	/**
	 * 
	 */
	public static final String TIME_ZONE = "timeZone";

	/**
	 * Available authentication types are DEFAULT, LDAP and PASSWORD. PASSWORD is used in cases where LDAP authentication is not possible, or the user does not
	 * exist in the directory.
	 * 
	 * @author mobreza
	 */
	public enum AuthenticationType {
		DEFAULT, LDAP, PASSWORD;
	}

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7049098087764831201L;

	private Long id;
	/** Unique user name */
	private String userName;
	/** User's e-mail address */
	private String mail;
	/** Last name */
	private String lastName;
	/** First name */
	private String firstName;
	/** Staff ID */
	private String staffId;
	/** Display name, contains all other names, titles, etc. */
	private String displayName;
	private String description;
	private String department;
	/**
	 * Current status of user account.
	 * 
	 * @see UserStatus
	 */
	private UserStatus status = UserStatus.ENABLED;
	/**
	 * Currently selected authentication mechanism.
	 * 
	 * @see AuthenticationType
	 */
	private AuthenticationType authenticationType = AuthenticationType.DEFAULT;
	/** Password, only stored when {@link AuthenticationType.PASSWORD} is used. */
	private String password;
	/** Last successful login */
	private Date lastLogin = null;
	/** Last failed login */
	private Date lastLoginFailed = null;

	private List<Preference> preferences;
	/**
	 * User roles assigned to user.
	 * 
	 * @see UserRole
	 */
	private List<UserRole> roles = new ArrayList();
	/**
	 * Aliases and similar data that uniquely links it to this user. In cases where user has multiple e-mail addresses and you need to find the user record from
	 * one of the available e-mails, those addresses should be listed in the {@link User#lookups} list.
	 * 
	 * @see UserLookup
	 */
	private List<UserLookup> lookups = new ArrayList();
	private GrantedAuthority[] grantedAuthorities;
	/**
	 * List of supervisors to this user.
	 * 
	 * @see UserSupervisor
	 * @see #subordinates
	 */
	private List<UserSupervisor> supervisors = new ArrayList<UserSupervisor>();
	/**
	 * List of subordinates of this user.
	 * 
	 * @see UserSupervisor
	 * @see #supervisors
	 */
	private List<UserSupervisor> subordinates = new ArrayList<UserSupervisor>();

	/**
	 * Reference to user impersonating current user
	 */
	private Authentication impersonator;

	/** Tag-based security */
	private List<String> accessTags = new ArrayList<String>();

	/**
	 * Default empty constructor
	 */
	public User() {

	}

	/**
	 * User unique identifier
	 * 
	 * @return the user id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * User's unique username
	 * 
	 * @return the userName
	 */
	@Column(name = "username", unique = true)
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * User's email address used when applications send automated emails to users.
	 * 
	 * @return the mail
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * Get email address in format <code>LastName, FirstName &lt;email@address&gt;</code>.
	 * 
	 * @return Address in format <code>LastName, FirstName &lt;email@address&gt;</code> or <code>null</code> if user has no email
	 */
	@Transient
	public String getSMTPAddress() {
		if (this.getMail() != null && this.getMail().length() > 0)
			return String.format("\"%1$s, %2$s\" <%3$s>", this.getLastName(), this.getFirstName(), this.getMail());
		else
			return null;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return this.department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the staffId
	 */
	public String getStaffId() {
		return this.staffId;
	}

	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	/**
	 * List of user's active roles. This is used for persistence purposes, also see {@link #getAuthorities()}.
	 * 
	 * @see UserRole
	 * @see #getAuthorities()
	 * @return list of user's roles
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	public List<UserRole> getRoles() {
		return this.roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
		// reset cached grantedAuthorities
		this.grantedAuthorities = null;
	}

	/**
	 * Return a list of user roles defined for user. A role <b>ROLE_USER</b> is automagically added.
	 * 
	 * @see org.springframework.security.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	@Transient
	public GrantedAuthority[] getAuthorities() {
		if (this.grantedAuthorities == null) {
			synchronized (this) {
				for (UserRole key : this.getRoles()) {
					User.LOG.trace("Role: " + key.getRole());
				}
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
				// insert authenticated user role
				grantedAuthorities.add(new GrantedAuthorityImpl("ROLE_USER"));
				// insert other roles
				for (UserRole role : this.getRoles()) {
					if ((role.getRole() == null) || (role.getRole().length() == 0)) {
						continue;
					}
					User.LOG.trace("Role: " + role.getRole() + " " + role.getId());
					grantedAuthorities.add(new GrantedAuthorityImpl(role.getRole()));
				}
				this.grantedAuthorities = grantedAuthorities.toArray(User.GRANTED_AUTHORITY_TYPE);
			}
		}
		return this.grantedAuthorities;
	}

	/**
	 * Grant an authority, but do not persist
	 * 
	 * @param role
	 */
	public synchronized void grantTemporaryAuthority(String role) {
		if (this.grantedAuthorities == null)
			getAuthorities();

		for (int i=0; i<this.grantedAuthorities.length; i++) {
			LOG.debug("User has old role " + i + ": " + this.grantedAuthorities[i]);
		}

		GrantedAuthority[] newAuthorities = new GrantedAuthority[this.grantedAuthorities.length + 1];
		for (int i = 0; i < this.grantedAuthorities.length; i++)
			newAuthorities[i] = this.grantedAuthorities[i];
		newAuthorities[newAuthorities.length - 1] = new GrantedAuthorityImpl(role);
		LOG.info("Granting temporary role: " + role);
		this.grantedAuthorities = newAuthorities;

		for (int i=0; i<this.grantedAuthorities.length; i++) {
			LOG.debug("User has new role " + i + ": " + this.grantedAuthorities[i]);
		}
	}

	/**
	 * Does user have a particular role?
	 */
	@Transient
	public boolean hasRole(String role) {
		for (GrantedAuthority ga : this.getAuthorities()) {
			if (ga.getAuthority().equalsIgnoreCase(role)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see org.springframework.security.userdetails.UserDetails#getPassword()
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set user password
	 * 
	 * @param password Clean-text password to use for authentication
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#getUsername()
	 */
	@Override
	@Transient
	public String getUsername() {
		return this.userName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonExpired ()
	 */
	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isCredentialsNonExpired ()
	 */
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isEnabled()
	 */
	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	@Enumerated
	public UserStatus getStatus() {
		return this.status;
	}

	/**
	 * @param lookups the lookups to set
	 */
	public void setLookups(List<UserLookup> lookups) {
		this.lookups = lookups;
	}

	/**
	 * Get list of aliases and similar data that uniquely links it to this user.
	 * 
	 * @return the lookups
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE })
	public List<UserLookup> getLookups() {
		return this.lookups;
	}

	/**
	 * Currently active authentication type for the user. When {@link AuthenticationType#PASSWORD} is selected, the password hash stored in
	 * {@link #getPassword()} should be used for authentication.
	 * 
	 * @return the authenticationType
	 */
	public AuthenticationType getAuthenticationType() {
		return this.authenticationType;
	}

	/**
	 * @param authenticationType the authenticationType to set
	 */
	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * @return the lastLoginFailed
	 */
	public Date getLastLoginFailed() {
		return this.lastLoginFailed;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @param lastLoginFailed the lastLoginFailed to set
	 */
	public void setLastLoginFailed(Date lastLoginFailed) {
		this.lastLoginFailed = lastLoginFailed;
	}

	/**
	 * Get User's supervisors
	 * 
	 * @see #getSubordinates()
	 * @return the supervisors
	 */
	@OneToMany(mappedBy = "user")
	public List<UserSupervisor> getSupervisors() {
		return this.supervisors;
	}

	/**
	 * Get User's subordinates
	 * 
	 * @see #getSupervisors()
	 * @return the subordinates
	 */
	@OneToMany(mappedBy = "supervisor")
	public List<UserSupervisor> getSubordinates() {
		return this.subordinates;
	}

	/**
	 * @param supervisors the supervisors to set
	 */
	public void setSupervisors(List<UserSupervisor> supervisors) {
		this.supervisors = supervisors;
	}

	/**
	 * @param subordinates the subordinates to set
	 */
	public void setSubordinates(List<UserSupervisor> subordinates) {
		this.subordinates = subordinates;
	}

	/**
	 * @param alias
	 */
	public void addAlias(String alias) {
		// check for existing user lookup
		for (UserLookup lookup : this.getLookups()) {
			if (lookup.getIdentifier().equalsIgnoreCase(alias)) {
				// found same identifier
				return;
			}
		}

		UserLookup ul = new UserLookup();
		ul.setIdentifier(alias);
		ul.setUser(this);
		this.getLookups().add(ul);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getLastName()).append(", ").append(this.getFirstName()).append(" [").append(this.getUsername()).append("]");
		return sb.toString();
	}

	public void addPref(Preference pref) {
		pref.setUser(this);
		this.preferences.add(pref);
	}

	/**
	 * Get user's TimeZone or the server default timezone if not specified
	 * 
	 * @return user's TimeZone or the server default timezone
	 */
	@Transient
	public TimeZone getTimeZone() {
		if (this.preferences == null) {
			return TimeZone.getDefault();
		}

		Preference p = this.findPreference(User.TIME_ZONE);
		String usrTzString = null;
		if (p != null) {
			usrTzString = String.valueOf(p.getPreferenceValue());
		}

		if (usrTzString == null) {
			return TimeZone.getDefault();
		} else {
			TimeZone usrTz = TimeZone.getTimeZone(usrTzString);
			return usrTz == null ? TimeZone.getDefault() : usrTz;
		}
	}

	public void setTimeZone(TimeZone timeZone) {
		this.setPreference(User.TIME_ZONE, timeZone.getID());
	}

	@Transient
	@Deprecated
	public String getTimeZoneID() {
		return this.getTimeZone().getID();
	}

	/**
	 * Get full name of user. Differs from {@link User#getDisplayName()} which returns display name value from user profile. This will return
	 * <code>FirstName LastName</code>.
	 */
	@Transient
	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getFirstName()).append(" ").append(this.getLastName());
		return sb.toString();
	}

	/**
	 * @param authentication the impersonator to set
	 */
	public void setImpersonator(Authentication authentication) {
		this.impersonator = authentication;
	}

	/**
	 * Get user that is impersonating this user
	 * 
	 * @return
	 */
	@Transient
	public Authentication getImpersonator() {
		return this.impersonator;
	}
	
	@Transient
	public String getUsernameImpersonated() {
		if (this.impersonator==null) return this.getUsername();
		return String.format("%1$s/%2$s", this.userName, this.impersonator.getName());
	}

	public void setPreference(String key, Object value) {
		Preference p = this.findPreference(key);
		p.setPreferenceValue(value);
	}

	public void addPref(String key, Object value) {
		Preference p = this.findPreference(key);
		if (p == null) {
			p = Preference.newInstance(key, value);
		}

		preferences.add(p);
	}

	public Preference findPreference(String key) {
		Preference pref = null;
		List<Preference> prefs = getPreferences();
		if (prefs == null)
			return null;
		for (Preference p : prefs) {
			if (key.equals(p.getPreferenceKey())) {
				return pref;
			}
		}

		return null;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	public List<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	@CollectionOfElements
	@JoinTable(name = "UserAccessTag")
	@Column(name = "tag", length = 500, nullable = false)
	public List<String> getAccessTags() {
		return this.accessTags;
	}

	public void setAccessTags(List<String> accessTags) {
		this.accessTags = accessTags;
	}

	@Override
	public int compareTo(User arg0) {
		int diff = -1;

		if (arg0 != null) {
			if (id != null && arg0.getId() != null) {
				diff = (int) (id - arg0.getId());
			}
		}

		return diff;
	}

	/**
	 * @see org.iita.security.UserAccess#hasAccess(org.iita.security.model.User)
	 */
	@Override
	public boolean hasAccess(User user) {
		return getId().equals(user.getId());
	}
}

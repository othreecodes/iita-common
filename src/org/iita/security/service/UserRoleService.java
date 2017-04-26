/**
 * 
 */
package org.iita.security.service;

import java.util.List;

import org.iita.security.model.User;
import org.iita.security.model.UserRole;

/**
 * The Interface UserRoleService.
 * 
 * @author aafolayan
 */
public interface UserRoleService {
	
	/**
	 * Find.
	 * 
	 * @param id the id
	 * 
	 * @return the user role
	 */
	public UserRole find(long id);

	/**
	 * Find by user.
	 * 
	 * @param user the user
	 * 
	 * @return the list< user role>
	 */
	public List<UserRole> findByUser(User user);

	/**
	 * Save.
	 * 
	 * @param userRole the user role
	 * 
	 * @return the string
	 */
	public String save(UserRole userRole);

	/**
	 * Removes the.
	 * 
	 * @param role the role
	 */
	public void remove(UserRole role);

	/**
	 * Find all.
	 * 
	 * @return the list< user role>
	 */
	public List<UserRole> findAll();

	/**
	 * Find all.
	 * 
	 * @param start the start
	 * @param maxResults the max results
	 * 
	 * @return the list< user role>
	 */
	public List<UserRole> findAll(int start, int maxResults);
}

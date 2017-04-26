/**
 * 
 */
package org.iita.security.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.security.model.User;
import org.iita.security.model.UserRole;
import org.iita.security.service.UserRoleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aafolayan
 * 
 */
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
	private static final Log log = LogFactory.getLog(UserRoleServiceImpl.class);
	private EntityManager em;

	/**
	 * @return the em
	 */
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 * @param em the em to set
	 */
	@PersistenceContext
	// DI operation goes here
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserRoleService#find(int)
	 */
	@Override
	public UserRole find(long id) {
		return em.find(UserRole.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserRoleService#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> findAll() {
		Query query = getEntityManager().createQuery("from UserRole u ");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserRoleService#findAll(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> findAll(int start, int maxResults) {
		Query query = getEntityManager().createQuery("from UserRole u").setFirstResult(start).setMaxResults(maxResults);
		return query.getResultList();
	}

	@Override
	public void remove(UserRole role) {
		if (role == null || role.getId() == null)
			return;
		this.em.remove(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iita.par.service.UserRoleService#save(org.iita.par.model.UserRole)
	 */
	@Override
	public String save(UserRole userRole) {
		if (userRole.getId() == null) {
			log.info("Adding role " + userRole.getApplication() + "." + userRole.getRole() + " to " + userRole.getUser());
			log.debug("new user role id is:  " + userRole.getId());
			em.persist(userRole);
			log.debug("new user role id is: now persisted!");
		}
		// update driver info
		else {
			log.info("Updating role " + userRole.getApplication() + "." + userRole.getRole() + " to " + userRole.getUser());
			log.debug("new updated user role id is:  " + userRole.getId());
			em.merge(userRole);
		}

		return "success";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> findByUser(User user) {
		log.info("Listing roles for " + user);
		return (List<UserRole>) em.createQuery("from UserRole u where u.user=:user ").setParameter("user", user).getResultList();
	}

}

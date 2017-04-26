package org.iita.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.entity.Notification;
import org.iita.security.model.User;
import org.iita.service.UserNotificationReader;
import org.iita.service.UserNotificationService;
import org.iita.util.PagedResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * This <b>"Local"</b> {@link UserNotificationService} implemenation will use JPA to retrieve messages.
 * 
 * @author mobreza
 */
public class LocalUserNotificationReaderImpl implements UserNotificationReader {
	private static final Log log = LogFactory.getLog(LocalUserNotificationReaderImpl.class);

	private EntityManager entityManager;

	@Override
	public Notification findNotification(Long id) {
		return entityManager.find(Notification.class, id);
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public int deleteAllMessages(User user) {
		log.debug("deleteAllMessages " + user);

		return entityManager.createQuery("delete from Notification n where n.subscriber=:user").setParameter("user", user).executeUpdate();
	}

	@Override
	@Transactional
	public void deleteMessage(Notification notification) {
		log.debug("deleteMessage " + notification);

		entityManager.remove(notification);
	}

	@Override
	@Transactional
	public void deleteMessage(Long id) {
		log.debug("deleteMessage " + id);

		try {
			Notification notification = entityManager.find(Notification.class, id);

			if (notification != null) {
				entityManager.remove(notification);
			}
		} catch (Exception e) {
			log.info("No notification with id " + id);
		}
	}

	@Override
	@Transactional
	public PagedResult<Notification> listMessages(User user, int startAt, int maxResults, boolean ascending) {
		log.debug("listMessages " + user + " " + startAt + " " + maxResults + " " + ascending);

		String orderBy = "order by id ASC";
		if (!ascending) {
			orderBy = "order by id DESC";
		}

		PagedResult<Notification> result = new PagedResult<Notification>(startAt, maxResults);
		result.setResults(entityManager.createQuery("from Notification n where n.subscriber=:user " + orderBy).setParameter("user", user).getResultList());
		result.setTotalHits((Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user").setParameter("user", user)
				.getSingleResult());
		return result;
	}

	@Override
	@Transactional
	public PagedResult<Notification> listUnread(User user, int startAt, int maxResults, boolean ascending) {
		log.debug("listUnread " + user + " " + startAt + " " + maxResults + " " + ascending);

		String orderBy = "order by id ASC";
		if (!ascending) {
			orderBy = "order by id DESC";
		}

		PagedResult<Notification> result = new PagedResult<Notification>(startAt, maxResults);
		result.setResults(entityManager.createQuery("from Notification n where n.subscriber=:user and n.read=false " + orderBy).setParameter("user", user)
				.getResultList());
		result.setTotalHits((Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user and n.read=false").setParameter(
				"user", user).getSingleResult());

		return result;
	}

	@Override
	@Transactional
	public void markAsRead(Notification notification) {
		log.debug("markAsRead " + notification);

		notification.setRead(true);
		entityManager.merge(notification);
	}

	@Override
	@Transactional
	public void markAsUnread(Notification notification) {
		log.debug("markAsUnread " + notification);

		notification.setRead(false);
		entityManager.merge(notification);
	}

	@Override
	@Transactional
	public long countMessages(User user) {
		log.debug("countMessages " + user);

		return (Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user").setParameter("user", user).getSingleResult();
	}

	@Override
	@Transactional
	public long countUnread(User user) {
		log.debug("countUnread " + user);

		return (Long) entityManager.createQuery("SELECT count(n) FROM Notification n where n.subscriber=:user and n.read=false").setParameter("user", user)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Notification> listAllMessages(User user) {
		log.debug("listAllMessages " + user);
		return entityManager.createQuery("from Notification n where n.subscriber=:user").setParameter("user", user).getResultList();
	}
}

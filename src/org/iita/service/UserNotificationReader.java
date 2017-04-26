/**
 * iita-common Jan 30, 2010
 */
package org.iita.service;

import java.util.List;

import org.iita.entity.Notification;
import org.iita.security.model.User;
import org.iita.util.PagedResult;

/*
 * *
 * 
 * @author mobreza
 */
public interface UserNotificationReader {

	public abstract Notification findNotification(Long id);

	public abstract PagedResult<Notification> listUnread(User user, int startAt, int maxResults, boolean ascending);

	public abstract PagedResult<Notification> listMessages(User user, int pageSize, int pageNumber, boolean ascending);

	public abstract long countMessages(User user);

	public abstract long countUnread(User user);

	public abstract List<Notification> listAllMessages(User user);

	public abstract void deleteMessage(Notification notification);

	public abstract void deleteMessage(Long id);

	public abstract int deleteAllMessages(User user);

	public abstract void markAsRead(Notification notification);

	public abstract void markAsUnread(Notification notification);

}
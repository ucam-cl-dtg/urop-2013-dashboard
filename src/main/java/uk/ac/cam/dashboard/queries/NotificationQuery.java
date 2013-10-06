package uk.ac.cam.dashboard.queries;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.cl.dtg.teaching.hibernate.HibernateUtil;
import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;

public class NotificationQuery extends PaginationQuery<NotificationQuery> {
	
	// Create the logger
	private static Logger log = LoggerFactory.getLogger(NotificationQuery.class);
	
	// Initialisation methods
	
	public NotificationQuery() {}
	
	public NotificationQuery(Criteria criteria) {
		this.criteria = criteria;
	}

	public static NotificationQuery all() {
		return new NotificationQuery (
			HibernateUtil.getInstance().getSession()
			.createCriteria(NotificationUser.class)
			.createAlias("notification", "n")
		);
	}
	
	// Query methods
	
	public static Notification get(int id) {
		Session session = HibernateUtil.getInstance().getSession();
		Notification n = (Notification) session
			.createQuery("from Notification where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
		return n;
	}
	
	public static NotificationUser getNU(int id) {
		Session session = HibernateUtil.getInstance().getSession();
		NotificationUser nu = (NotificationUser) session
			.createQuery("from NotificationUser where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
		return nu;
	}
	
	public NotificationQuery byNotification(Notification notification) {
		log.debug("Getting notification id: " + notification.getId());
		criteria.add(Restrictions.eq("notification", notification));
	return this;
	}
	
	public NotificationQuery byUser(User user) {
			log.debug("Getting notifications for user: " + user.getCrsid());
			criteria.add(Restrictions.eq("user", user));
		return this;
	}
	
	public NotificationQuery inSection(String section) {
		criteria.add(Restrictions.eq("n.section", section));
		return this;
	}
	
	public NotificationQuery foreignId(String foreignId) {
		criteria.add(Restrictions.eq("n.foreignId", foreignId));
		return this;
	}
	
	public NotificationQuery isRead(boolean read) {
		this.criteria.add(Restrictions.eq("read", read));
		return this;
	}
	
	// Result methods
	
	@SuppressWarnings("unchecked")
	public List<NotificationUser> list() {
		return this.criteria.addOrder(Order.desc("n.timestamp")).list();
	}

	public NotificationUser uniqueResult() {
		NotificationUser notificationUser = (NotificationUser)criteria.uniqueResult();
		return notificationUser;
	}

//	public HashMap<String, ?> map() {
//		NotificationUser notificationUser = (NotificationUser)criteria.uniqueResult();
//		
//		return notificationUser;
//	}
	
}

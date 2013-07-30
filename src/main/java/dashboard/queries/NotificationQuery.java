package dashboard.queries;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import uk.ac.cam.dashboard.models.Notification;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

public class NotificationQuery {
	
	private Criteria criteria;
	
	public NotificationQuery() {}
	
	public NotificationQuery(Criteria criteria) {
		this.criteria = criteria;
	}

	public static NotificationQuery all() {
		return new NotificationQuery (
			HibernateUtil.getTransactionSession()
			.createCriteria(Notification.class)
			.addOrder(Order.desc("timestamp"))
		);
	}
	
	public NotificationQuery forUser(User user) {
		criteria.createAlias("users", "u")
			.add(Restrictions.eq("u.crsid", user.getCrsid()));
		return this;
	}
	
	public NotificationQuery offset(int offset) {
		this.criteria.setFirstResult(offset);
		return this;
	}

	public NotificationQuery limit(int limit) {
		this.criteria.setMaxResults(limit);
		return this;
	}
	
	public NotificationQuery inSection(String section) {
		this.criteria.add(Restrictions.eq("section", section));
		return this;
	}
	
	public NotificationQuery isRead(boolean read) {
		this.criteria.add(Restrictions.eq("read", read));
		return this;
	}
	
	public List<Notification> list() {
		return this.criteria.list();
	}
	
}

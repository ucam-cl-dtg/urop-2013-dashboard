package uk.ac.cam.dashboard.queries;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

public class UserQuery {
	
	// Create the logger
	private static Logger log = LoggerFactory.getLogger(UserQuery.class);
	
	private Criteria criteria;
	
	public UserQuery() {}
	
	public UserQuery(Criteria criteria) {
		this.criteria = criteria;
	}

	public static UserQuery all() {
		return new UserQuery (
			HibernateUtil.getTransactionSession()
			.createCriteria(User.class)
			.addOrder(Order.asc("username"))
		);
	}
	
	public static User get(String id) {
		Session session = HibernateUtil.getTransactionSession();
		User u = (User) session
			.createQuery("from User where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
			return u;
	}
	
	public UserQuery byCrsid(String crsid) {
		log.debug("Getting user with crsid: " + crsid);
		criteria.add(Restrictions.eq("crsid", crsid));
	return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> list() {
		return this.criteria.list();
	}

	public User uniqueResult() {
		User user = (User)criteria.uniqueResult();
		return user;
	}
	
}

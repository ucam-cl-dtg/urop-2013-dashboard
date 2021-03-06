package uk.ac.cam.dashboard.queries;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.cl.dtg.teaching.hibernate.HibernateUtil;
import uk.ac.cam.dashboard.models.Deadline;
import uk.ac.cam.dashboard.models.DeadlineUser;
import uk.ac.cam.dashboard.models.User;

public class DeadlineQuery extends PaginationQuery<DeadlineQuery> {
	
	// Create the logger
	private static Logger log = LoggerFactory.getLogger(DeadlineQuery.class);
	
	public DeadlineQuery() {}
	
	public DeadlineQuery(Criteria criteria) {
		this.criteria = criteria;
	}

	public static DeadlineQuery created() {
		return new DeadlineQuery (
			HibernateUtil.getInstance().getSession()	
			.createCriteria(Deadline.class, "d")
		);
	}
	
	public static DeadlineQuery set() {
		return new DeadlineQuery (
			HibernateUtil.getInstance().getSession()
			.createCriteria(DeadlineUser.class)
			.createAlias("deadline", "d")
		);
	}
	
	public static Deadline get(int id) {
		Session session = HibernateUtil.getInstance().getSession();
		Deadline n = (Deadline) session
			.createQuery("from Deadline where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
			return n;
	}
	
	public static DeadlineUser getDUser(int id) {
		Session session = HibernateUtil.getInstance().getSession();
		DeadlineUser n = (DeadlineUser) session
			.createQuery("from DeadlineUser where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
			return n;
	}
	
	public DeadlineQuery byDeadline(Deadline Deadline) {
		log.debug("Getting Deadline id: " + Deadline.getId());
		criteria.add(Restrictions.eq("d", Deadline));
		return this;
	}
	
	public DeadlineQuery byUser(User user) {
		log.debug("Getting Deadlines for user: " + user.getCrsid());
		criteria.add(Restrictions.eq("user", user));
		return this;
	}
	
	public DeadlineQuery byOwner(User user) {
		log.debug("Getting Deadlines created by user: " + user.getCrsid());
		criteria.add(Restrictions.eq("owner", user));
		return this;
	}
	
	public DeadlineQuery isArchived(boolean archived) {
		log.debug("Getting archived Deadlines");
		criteria.add(Restrictions.eq("archived", archived));
		return this;
	}
	public DeadlineQuery isComplete(boolean complete) {
		log.debug("Getting completed Deadlines");
		criteria.add(Restrictions.eq("complete", complete));
		return this;
	}
	public DeadlineQuery beforeDate(Calendar date) {
		log.info("Getting deadlines before " + date.getTime().toString());
		criteria.add(Restrictions.le("d.datetime", date));
		return this;
	}
	public DeadlineQuery afterDate(Calendar date) {
		log.info("Getting deadlines after " + date.getTime().toString());
		criteria.add(Restrictions.ge("d.datetime", date));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeadlineUser> setList() {
		return this.criteria.addOrder(Order.asc("d.datetime")).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Deadline> createdList() {
		return this.criteria.addOrder(Order.asc("d.datetime")).list();
	}

	public DeadlineUser uniqueResult() {
		DeadlineUser DeadlineUser = (DeadlineUser)criteria.uniqueResult();
		return DeadlineUser;
	}
	
//	public HashMap<String, ?> map() {
//		DeadlineUser DeadlineUser = (DeadlineUser)criteria.uniqueResult();
//		
//		return DeadlineUser;
//	}
	
}

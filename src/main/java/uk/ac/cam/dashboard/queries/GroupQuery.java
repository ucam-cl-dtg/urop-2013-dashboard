package uk.ac.cam.dashboard.queries;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.Group;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

public class GroupQuery {
	
	// Create the logger
	private static Logger log = LoggerFactory.getLogger(GroupQuery.class);
	
	private Criteria criteria;
	
	public GroupQuery() {}
	
	public GroupQuery(Criteria criteria) {
		this.criteria = criteria;
	}

	public static GroupQuery all() {
		return new GroupQuery (
			HibernateUtil.getTransactionSession()
			.createCriteria(Group.class)
			.addOrder(Order.asc("name"))
		);
	}
	
	public static Group get(int id) {
		Session session = HibernateUtil.getTransactionSession();
		Group g = (Group) session
			.createQuery("from Group where id = :id")
			.setParameter("id", id)
			.uniqueResult();
			
			return g;
	}
	
	public GroupQuery byGroup(Group group) {
		log.debug("Getting Group id: " + group.getId());
		criteria.add(Restrictions.eq("Group", group));
	return this;
	}
	
	public GroupQuery byMember(User user) {
			log.debug("Getting all Groups with member: " + user.getCrsid());
			criteria.createAlias("users", "u");
			criteria.add(Restrictions.eq("u.crsid", user.getCrsid()));
		return this;
	}
	
	public GroupQuery byOwner(User user) {
		log.debug("Getting Groups created by user: " + user.getCrsid());
		criteria.add(Restrictions.eq("owner", user));
	return this;
	}
	
	public GroupQuery byName(String name) {
		criteria.add(Restrictions.like("title", "%"+name+"%"));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<Group> list() {
		return this.criteria.list();
	}

	public Group uniqueResult() {
		Group group = (Group)criteria.uniqueResult();
		return group;
	}
	
}

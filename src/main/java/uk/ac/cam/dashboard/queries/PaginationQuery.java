package uk.ac.cam.dashboard.queries;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

public class PaginationQuery<T> {
	
	protected Criteria criteria;
	
	public PaginationQuery<T> offset(int offset) {
		this.criteria.setFirstResult(offset);
		return this;
	}

	public PaginationQuery<T> limit(int limit) {
		this.criteria.setMaxResults(limit);
		return this;
	}
	
	public int totalRows() {
		Integer totalRows = ((Long) this.criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		// Reset query
		this.criteria.setProjection(null);
		this.criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		
		return totalRows;
	}
	
}

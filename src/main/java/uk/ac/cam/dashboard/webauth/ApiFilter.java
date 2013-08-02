package uk.ac.cam.dashboard.webauth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.controllers.ApiController;
import uk.ac.cam.dashboard.models.Api;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

public class ApiFilter implements Filter {
	
	public static enum Permissions {
		NONE,
		USER,
		GLOBAL
	}
	
	private static Logger log = LoggerFactory.getLogger(ApiFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initializing API filter...");
	}

	@Override
	public void doFilter(ServletRequest servletReq, ServletResponse servletResp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletReq;
		HttpServletResponse response = (HttpServletResponse) servletResp;
		HttpSession session = request.getSession();
		
		String user = (String) request.getParameter("user");
		String auth = (String) request.getParameter("auth");
		String ravenUser = (String) session.getAttribute("RavenRemoteUser");
		
		System.out.println(Arrays.toString(request.getParameterMap().keySet().toArray()));
		System.out.println("User: " + user);
		System.out.println("Auth: " + auth);
		System.out.println("RavenUser: " + ravenUser);
		
		if (ravenUser != null) {
			log.debug("No need for API validation");
			session.setAttribute("permissions", Permissions.USER);
			
			chain.doFilter(request, response);
			return;
		} else if (auth != null) {
			if ( validateGlobalApiKey(auth) ) {
				log.debug("Passed API validation...");
				session.setAttribute("permissions", Permissions.GLOBAL);
				
				chain.doFilter(request, response);
				return;
			} else if (user != null) { 
				if ( validateApiKeyForUser(auth, user) ) {
					log.debug("Passed API validation...");
					session.setAttribute("permissions", Permissions.USER);
					
					chain.doFilter(request, response);
					return;
				}
			}
		}
		
		log.error("Failed API validation...");
		session.setAttribute("permissions", Permissions.NONE);
			
		response.sendError(401, "Failed API validation");
		
	}

	@Override
	public void destroy() {
	}
	
	// Validation
	
	public boolean validateApiKeyForUser(String key, String userCrsid) {
		User user = User.registerUser(userCrsid);
		
		if (user != null) {
		
			for (Api a : user.getApis()) {
				if ( key.equals(a.getKey()) ) {
					return true;
				}
			}
		
		}
		
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean validateGlobalApiKey(String key) {
		Session s = HibernateUtil.getTransactionSession();
		
		Criteria criteria = s.createCriteria(Api.class);
		criteria.add(Restrictions.eq("globalPermissions", true));
		List<Api> apis = criteria.list();
		
		for ( Api a : apis ) {
			if ( a.getKey().equals(key) ) {
				return true;
			}
		}
		
		return false;
		
	}
	
}

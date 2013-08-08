package uk.ac.cam.dashboard.util;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.controllers.AccountController;
import uk.ac.cam.dashboard.controllers.ApiController;
import uk.ac.cam.dashboard.controllers.DeadlinesController;
import uk.ac.cam.dashboard.controllers.GroupsController;
import uk.ac.cam.dashboard.controllers.HomePageController;
import uk.ac.cam.dashboard.controllers.NotificationsController;
import uk.ac.cam.dashboard.controllers.ProjectSettingsController;
import uk.ac.cam.dashboard.controllers.SupervisorController;

import com.googlecode.htmleasy.HtmleasyProviders;

public class Dashboard extends Application {
	
  private static Logger log = LoggerFactory.getLogger(Dashboard.class);
	
  public Set<Class<?>> getClasses() {
    Set<Class<?>> myServices = new HashSet<Class<?>>();
    
    // Add controllers    
    log.debug("Adding controllers to main application");
    myServices.add(ProjectSettingsController.class);
    myServices.add(AccountController.class);
    myServices.add(DeadlinesController.class);
    myServices.add(GroupsController.class);
    myServices.add(NotificationsController.class);
    myServices.add(HomePageController.class);
    myServices.add(ApiController.class);
    myServices.add(SupervisorController.class);
    
    // Add Htmleasy Providers
    log.debug("Adding Htmleasy providers");
    myServices.addAll(HtmleasyProviders.getClasses());

    return myServices;
  }
}

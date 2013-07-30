/* Demo Routes:

The first term must be the route from where the template will get the data.
The second term must be either a string representing the template name or
a function that returns the template name. The function will receive the json returned
by the request as the first parameter.

$(document).ready(function() {
    router = Router({
        "test(/:id)": "main.test",
        //For getting params in get requests
        "search?:params" : "main.searchtemplate",
        "tester": function(json) { return json['isSupervisor'] ? "a" : "b";}
        // Use the last line to redirect unmatched routes to an error page
        "*undefined": "errors.notfound"
    })
})
*/
function supportRedirect(templateName) {
    return function(json) {
        if (json.redirect) 
            router.navigate(json.redirect, {trigger: true});
        return templateName;
    }
}

$(document).ready(function() {
    router = Router({
        
        // Home page
        "dashboard/": "dashboard.home_page.index",

        // Deadlines
        "dashboard/deadlines" : "dashboard.deadlines.index",
        "dashboard/deadlines/:id/edit" : supportRedirect("dashboard.deadlines.edit"),
        "dashboard/deadlines/error/:type": "dashboard.deadlines.index",
        
        // Groups
        
        "dashboard/groups": "dashboard.groups.index",
        "dashboard/groups/error/:type": "dashboard.groups.index",
        "dashboard/groups/:id/edit" : "dashboard.groups.edit",
        
        // Notifications
        "dashboard/notifications/:userID": "dashboard.notifications.notifications",
        "dashboard/notifications/create": "dashboard.notifications.createNotification",

        //For getting params in get requests
        // Use the last line to redirect unmatched routes to an error page
        //"*undefined": "errors.notfound"
    });
});


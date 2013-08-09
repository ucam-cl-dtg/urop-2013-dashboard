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
    };
}


$(document).ready(function() {
    router = Router({
        
        // Home page
        "": "dashboard.home.index",
        
        // Account
        "account": "dashboard.account.index",

        // Deadlines
        "deadlines" : "dashboard.deadlines.index",
        "deadlines/:id" : "dashboard.deadlines.manage",
        "deadlines/:id/edit" : "dashboard.deadlines.edit",
        
        // Groups
        "groups": "dashboard.groups.index",
        "groups/:id" : "dashboard.groups.manage",
        "groups/:id/edit" : "dashboard.groups.edit",
        
        // Notifications
        "notifications(:params*)": "dashboard.notifications.index",
        "notifications/archive(:params*)": "dashboard.notifications.index",
        
        // Supervisor
        "supervisor" : "dashboard.supervisor.index"
        
        //For getting params in get requests
        // Use the last line to redirect unmatched routes to an error page
        //"*undefined": "errors.notfound"
    });
});


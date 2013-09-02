function createGroup() {
	submitAjaxForm("newGroupForm", "#newGroupSection", "dashboard.supervisor.newgroup", "Successfully created group");
}
function importGroup() {
	submitAjaxForm("importGroupForm", "#importGroupSection", "dashboard.supervisor.importgroup", "Successfully imported group");
}
function addSupervisor() {
	submitAjaxForm("addSupervisorForm", "#addSupervisorSection", "dashboard.supervisor.addsupervisor", "Added supervisor succesfully");
}
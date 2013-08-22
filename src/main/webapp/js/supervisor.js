function createDeadline() {
	submitAjaxForm("newDeadlineForm", "#newDeadlineSection", "dashboard.supervisor.newdeadline", "Successfully created deadline");
}
function createGroup() {
	submitAjaxForm("newGroupForm", "#newGroupSection", "dashboard.supervisor.newgroup", "Successfully created group");
}
function importGroup() {
	submitAjaxForm("importGroupForm", "#importGroupSection", "dashboard.supervisor.importgroup", "Successfully imported group");
}
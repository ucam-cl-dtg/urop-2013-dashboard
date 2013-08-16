function createDeadline() {
	submitAjaxForm("newDeadlineForm", "#newDeadlineSection", "dashboard.supervisor.newdeadline");
}
function createGroup() {
	submitAjaxForm("newGroupForm", "#newGroupSection", "dashboard.supervisor.newgroup");
}
function importGroup() {
	submitAjaxForm("importGroupForm", "#importGroupSection", "dashboard.supervisor.importgroup");
}
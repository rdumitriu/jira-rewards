(function ($) {

    function initDialogTrigger(context){
        context.find("a.rwd-trigger-dialog").each(function () {
            new JIRA.FormDialog({
                trigger: this,
                id: this.id + "-dialog",
                ajaxOptions: {
                    url: this.href,
                    data: {
                        decorator: "dialog",
                        inline: "true"
                    }
                }
            });
        });
    }

    function createRewardTypePicker(context) {
        context.find("select.rwd-select").each(function (i, el) {
            new AJS.SingleSelect({
                element: el,
                revertOnInvalid: true
            });
        });
    }

    JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, context, reason) {
        if (reason !== JIRA.CONTENT_ADDED_REASON.panelRefreshed) {
            createRewardTypePicker(context);
        }
    });

    JIRA.bind(JIRA.Events.ISSUE_REFRESHED, function (e, context, reason) {
        initDialogTrigger(context);
    });
})(AJS.$);
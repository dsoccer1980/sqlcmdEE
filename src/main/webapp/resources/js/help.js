function initHelp(ctx) {
    $.get(ctx + "/help/content", function (elements) {
        $("#loading").hide();
        $('#help script[template="row"]').tmpl(elements).appendTo('#help .container');
    });
}
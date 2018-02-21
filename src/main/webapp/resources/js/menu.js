function initMenu(ctx) {
    $.get(ctx + "/menu/content", function (elements) {
        $("#loading").hide();
        $('#menu script[template="row"]').tmpl(elements).appendTo('#menu .container');
    });
}
function initFind(ctx) {
    var url = window.location.href;
    var parts = url.split('/');
    var tableName = parts[parts.length - 1];

    $.get(ctx + "/find/' + tableName +'/content", function (elements) {
        $("#loading").hide();
        $('#find script[template="row"]').tmpl(elements).appendTo('#find .container');
    });
}
function initTables(ctx) {
    $.get(ctx + "/tables/content", function (elements) {
        $("#loading").hide();
        $('#tables script[template="row"]').tmpl(elements).appendTo('#tables .container');
    });
}
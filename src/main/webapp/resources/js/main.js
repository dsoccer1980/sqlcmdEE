function init(ctx) {

    var initMenu = function() {
        $('#menu').show();
        $('#loading').show();

        $.get(ctx + "/menu/content", function (elements) {
            $("#loading").hide();
            $('#menu script[template="row"]').tmpl(elements).appendTo('#menu .container');
        });
    };

    var initHelp = function() {
        $('#help').show();
        $('#loading').show();

        $.get(ctx + "/help/content", function (elements) {
            $("#loading").hide();
            $('#help script[template="row"]').tmpl(elements).appendTo('#help .container');
        });
    };

    var initTables = function () {
        $('#tables').show();
        $('#loading').show();

        $.get(ctx + "/tables/content", function (elements) {
            $("#loading").hide();
            $('#tables script[template="row"]').tmpl(elements).appendTo('#tables .container');
        });
    };

    var initFind = function () {
        $('#find').show();
        $('#loading').show();

        var url = window.location.href;
        var parts = url.split('/');
        var tableName = parts[parts.length - 1];

        $.get(ctx + "/find/' + tableName +'/content", function (elements) {
            $("#loading").hide();
            $('#find script[template="row"]').tmpl(elements).appendTo('#find .container');
        });
    };

    var hideAllScreens = function () {
        $('#help').hide();
        $('#menu').hide();
        $('#tables').hide();
        $('#find').hide();
    }

}
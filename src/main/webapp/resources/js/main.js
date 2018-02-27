function init(ctx) {

    var fromPage = null;

    var showFromPage = function() {
        window.location.hash = fromPage;
        fromPage = null;
    };

    var isConnected = function(url, onConnected) {
        $.get(ctx + "/connected", function(userName) {
            if (userName == "") {
                fromPage = url;
                window.location.hash = '/connect';
            } else {
                if (!!onConnected) {
                    onConnected(userName);
                }
            }
        });
    };

    var show = function(selector) {
        var component = $(selector);
        component.find('.container').children().not(':first').remove();
        component.show();
    };

    var intTables = function() {
        isConnected("tables", function() {
            show('#tables');

            $.get(ctx + "/tables/content", function(elements) {
                $("#loading").hide(300, function() {
                    $('#tables script').tmpl(elements).appendTo('#tables .container');
                });
            });
        });
    };

    var initFind = function(tableName) {
        isConnected("find/" + tableName, function() {
            show('#find');

            $.get(ctx + '/find/' + tableName + '/content', function(elements) {
                $('#loading').hide(300, function() {
                    $('#find script').tmpl(elements).appendTo('#find .container');
                });
            });
        });
    };

    var initMenu = function() {
        show('#menu');

        $.get(ctx + "/menu/content", function(elements) {
            $("#loading").hide(300, function() {
                $('#menu script').tmpl(elements).appendTo('#menu .container');
            });
        });
    };

    var initConnect = function() {
        $("#database").val("");
        $("#username").val("");
        $("#password").val("");
        $('#error').hide();
        $('#error').html("");
        $("#loading").hide(300, function() {
            $('#connecting-form').show();
        });
    };

    var initCreate = function() {
        isConnected("create/", function() {
            $("#tablename").val("");
            $("#column1").val("");
            $("#column2").val("");
            $("#column3").val("");
            $('#messageCreate').hide();
            $('#messageCreate').html("");
            $("#loading").hide(300, function () {
                $('#create-form').show();
            });
        });
    };

    var initHelp = function() {
        show('#help');

        $.get(ctx + "/help/content", function(elements) {
            $("#loading").hide(300, function() {
                $('#help script').tmpl(elements).appendTo('#help .container');
            });
        });
    };

    var initActions = function(userName) {
        isConnected("actions", function(userName) {
            show('#actions');

            $.get(ctx + '/actions/' + userName + '/content', function(elements) {
                $("#loading").hide(300, function() {
                    $('#actions script').tmpl(elements).appendTo('#actions .container');
                });
            });
        });
    };

    var initClear = function(tableName) {
            isConnected("clear/" + tableName, function() {
                $.get(ctx + '/clear/' + tableName + '/content', function(message) {
                      $("#loading").hide(300, function() {
                          $('#messageClear').show();
                          $('#messageClear').html(message);
                      });
                });
            });
    };

    var hideAllScreens = function() {
        $('#find').hide();
        $('#tables').hide();
        $('#menu').hide();
        $('#help').hide();
        $('#actions').hide();
        $('#connecting-form').hide();
        $('#create-form').hide();
        $('#messageClear').hide();
        $('#messageClear').html("");
    };

    var loadPage = function(data) {
        hideAllScreens();
        $("#loading").show();

        var page = data[0];
        if (page == 'tables') {
            intTables();
        } else if (page == 'find') {
            initFind(data[1]);
        } else if (page == 'menu') {
            initMenu();
        } else if (page == 'help') {
            initHelp();
        } else if (page == 'connect') {
            initConnect();
        } else if (page == 'actions') {
            initActions(data[1]);
        } else if (page == 'create') {
            initCreate();
        } else if (page == 'clear') {
            initClear(data[1]);
        } else {
            window.location.hash = "/menu";
        }
    };

    var load = function() {
        var hash = window.location.hash.substring(1);
        var parts = hash.split('/');
        if (parts[0] == '') {
            parts.shift();
        }
        loadPage(parts);
    };

    $(window).bind('hashchange', function(event) {
        load();
    });

    $('#connect').click(function() {
        var connection = {};
        connection.database = $("#database").val();
        connection.userName = $("#username").val();
        connection.password = $("#password").val();

        $.ajax({
            url: ctx + "/connect",
            data: connection,
            type: 'PUT',
            success: function(message) {
                if (message == "" || message == null) {
                    showFromPage();
                } else {
                    $('#error').html(message);
                    $('#error').show();
                }
            }
        });
    });

    $('#create').click(function() {
        var table = {};
        table.tableName = $("#tablename").val();
        table.column1 = $("#column1").val();
        table.column2 = $("#column2").val();
        table.column3 = $("#column3").val();

        $.ajax({
            url: ctx + "/create",
            data: table,
            type: 'PUT',
            success: function(message) {
                $('#messageCreate').html(message);
                $('#messageCreate').show();
            }
        });
    });

    load();
}

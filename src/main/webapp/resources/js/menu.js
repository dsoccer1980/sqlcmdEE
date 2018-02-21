$(window).on('load',function() {

    $.get('menu/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        $('#menu script[template="row"]').tmpl(elements).appendTo('#menu .container');

    });
});
$(window).on('load',function() {

    $.get('menu/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        $('#menu row-template').tmpl(elements).appendTo('#menu .container');

    });
});
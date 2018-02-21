$(window).on('load',function() {

    $.get('tables/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        $('#tables script[template="row"]').tmpl(elements).appendTo('#tables .container');

    });
});
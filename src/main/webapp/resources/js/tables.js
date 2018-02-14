$(window).on('load',function() {

    $.get('tables/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        $('#tables row-template').tmpl(elements).appendTo('#tables .container');

    });
});
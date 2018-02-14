$(window).on('load',function() {

    $.get('find/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        $('#find row-template').tmpl(elements).appendTo('#find .container');

    });
});
$(window).on('load',function() {

    $.get('help/content', function(elements) {
        $("#loading").hide();
            var container = $("#commands");
            $('#help row-template').tmpl(elements).appendTo('#help .container');
    });
});
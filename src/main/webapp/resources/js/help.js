$(window).on('load',function() {

    $.get('help/content', function(elements) {
        $("#loading").hide();
            var container = $("#commands");
            $('#help script[template="row"]').tmpl(elements).appendTo('#help .container');
    });
});
$(window).on('load',function() {

    $.get('help/content', function(elements) {
        $("#loading").hide();
            var container = $("#commands");
            $('#descriptionRow').tmpl(elements).appendTo('#commands');
    });
});
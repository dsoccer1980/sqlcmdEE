$(window).on('load',function() {

    $.get('/sqlcmd/find/content', function(elements) {
        $("#loading").hide();

        $('#find #temp').tmpl(elements).appendTo('#find .container');

    });
});
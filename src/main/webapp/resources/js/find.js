$(window).on('load',function() {
    var url = window.location.href;
    var parts = url.split('/');
    var tableName = parts[parts.length - 1];

    $.get('/sqlcmd/find/' + tableName +'/content', function(elements) {
        $("#loading").hide();

        $('#find #temp').tmpl(elements).appendTo('#find .container');

    });
});
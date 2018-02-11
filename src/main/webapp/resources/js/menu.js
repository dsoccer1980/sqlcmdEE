$(window).on('load',function() {

    $.get('menu/content', function(elements) {
        $("#loading").hide();
        var container = $("#menu_container");
        for (var index in elements) {
            var element = elements[index];
            container.append('<a href="' + element + '">' + element + '</a><br>');
        }
    });
});
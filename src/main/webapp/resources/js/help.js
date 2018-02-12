$(window).on('load',function() {

    $.get('help/content', function(elements) {
        $("#loading").hide();
        var container = $("#commands");
        for (var index in elements) {
            var element = elements[index];
            container.append(element.command + "<br>" + element.description + "<br>");
        }
    });
});
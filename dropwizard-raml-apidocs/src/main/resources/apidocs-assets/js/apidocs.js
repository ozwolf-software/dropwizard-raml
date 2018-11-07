function toggleVisibility(id){
    var element = $(id);

    if (element.hasClass('invisible')){
        element.removeClass('invisible');
        element.removeClass('d-none');
        element.addClass('visible');
        element.addClass('d-block');
    } else {
        element.removeClass('visible');
        element.removeClass('d-block');
        element.addClass('invisible');
        element.addClass('d-none');
    }
}

function bootstrap() {
    $(".hover-highlight").each(function(i, element) {
        $(element).hover(
            function() {
                $(this).addClass("border-secondary").addClass("bg-light");
            },
            function() {
                $(this).removeClass("border-secondary").removeClass("bg-light");
            }
        )
    });
}
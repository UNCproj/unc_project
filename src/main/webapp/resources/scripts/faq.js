$(function(){
    $("div.question").bind("click", function() {
        if($(this).hasClass("question-active")) {
            $(this).removeClass("question-active");
            $(this).next("div.answer:first").slideUp(500);
            $(this).find("span").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
        }else{
            //$("div.question").removeClass("question-active");
            //$("div.question span").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
            //$("div.answer").slideUp(500);
            $(this).addClass("question-active");
            $(this).next("div.answer:first").slideDown(500);
            $(this).find("span").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
        }
    });
});
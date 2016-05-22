$(function(){

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    };

    $(".delete-forum span").on('click',function(){
        console.log("Нажали на удаление");
        var id = this.getAttribute("id")

        $.ajax({
            type: 'POST',
            url: "/unc-project/delete",
            data: {"id": id}
        }).done(function (data) {
            if (data == 0 || data == '0'){
                //var hr = document.location.href;
                //hr = hr.substring(0,hr.indexOf("forum.jsp?"));
                //
                //if ($.urlParam('id')!=null) {
                //    window.location.replace(document.location.href);
                //}
                //if ($.urlParam('type')!=null) {
                    window.location.replace(document.location.href);

            }
        });
    });
});
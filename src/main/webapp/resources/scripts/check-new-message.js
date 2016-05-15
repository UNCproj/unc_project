/**
 * Created by alex on 15.05.2016.
 */
$(function(){
    var load = function(){
        $.ajax({
            type: "POST",
            url: "checkNewMessage",
            data: "action=load",
            success: function(data){
                //console.log( data );
                if (data == -1){
                    $('div.new-mess').remove();
                    //console.log("учетная занить");
                } else if(data == 0 || data == '0'){
                    $('div.new-mess').remove();
                    load();
                }else{
                    $('div.new-mess').remove();
                    console.log("У вас " + data + "непрочитанных сообщений!");

                    $(".content").prepend('<div class="new-mess" title="Новых сообщений - ' + data + '">У вас новое сообщение</div>');
                    //$(".content").prepend('<div class="new-mess" >У вас новое сообщение</div>');

                    load();
                }
            }
        });
    };
    load();
});
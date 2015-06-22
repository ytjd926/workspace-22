$(function(){

    $.ajax(url+"/getDict?name=single1")
        .done(function(data){
            $("#inputSingle1").val(data.value);
        }).fail(function(error){
        });
    $.ajax(url+"/getDict?name=single2")
        .done(function(data){
            $("#inputSingle2").val(data.value);
        }).fail(function(error){
        });




    $("#saveDict").click(function(){
       var single1 =  $("#inputSingle1").val();
        var single2 =  $("#inputSingle2").val();

        $.ajax(url+"/saveDict?name=single1&value="+single1)
            .done(function(data){
            }).fail(function(error){
            });

        $.ajax(url+"/saveDict?name=single2&value="+single2)
            .done(function(data){
            }).fail(function(error){
            });


    });
});
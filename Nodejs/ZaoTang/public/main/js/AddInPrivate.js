$(function(){
    /**
     * 1、要加上必须输数字的验证
     * 2、添加房间时，验证房间号是否唯一 **
     */
    $.ajax(url+"/getDict?name=single1")
        .done(function(data){
        window.single1 = data.value;
        }).fail(function(error){
        });
    $.ajax(url+"/getDict?name=single2")
        .done(function(data){
            window.single2 = data.value;
        }).fail(function(error){
        });




    //init本页
    var init=function(){
        //查询出所有房间
        $.ajax(url+"/findAllRoom")
            .done(function(data){
                if(data!=""){

                    for(var i=0;i<data.length;i++){
                        var room=data[i];
                        var $floor1=$("#floor1");
                        var $floor2=$("#floor2");

                       var html= createRoom("room"+room.roomName,room.roomName,room.isSingle,room.usedTime);
                        if(room.floor=="1"){
                            $floor1.append(html);
                        }else if(room.floor=="2"){
                            $floor2.append(html);
                        }


                    }

                    $("div[timer]").each(function(){
                        var $t=$(this);
                        console.log($t.attr("id"));

                    });



                }else{
                    console.log("没有查询到数据");
                }

            }).fail(function(){
                alert("出错啦！");
            });

    };
    init();


    //显示一楼添加弹层
    $('#btnAdd').click(function (e) {
        e.preventDefault();
        $('#myModal').modal('show');
    });

    //点击添加房间按钮
    $("#addRoomBy1").click(function(){
        var roomCode=$("#addRoomBy1RoomCode").val();
        var radioValue=$('input:radio[name=addRoomBy1Radio]:checked').val();

//        alert(roomCode +"   "+ radioValue);

        $.ajax(url+"/saveRoom?roomCode="+roomCode+"&isSingle="+radioValue+"&floor="+1)
            .done(function(data){
//                alert("哈哈，成功了！"+data);
                init();
            }).fail(function(){
                alert("出错啦！");
            });


    });


    var createRoom=function(roomCode,code,isSingle,userdTime){

        var time="";
        var timeTip="";
        var titleColor = "#73a839";
        var buttonClass = "btn btn-success btn-sm";
        var buttonValue = "使用";
        if(userdTime!=null && userdTime!=undefined && userdTime!=""){
            var time=new Date(userdTime);
            var nowTime = new Date();
            time = nowTime.getTime()-time.getTime();
            time=parseInt(time/1000/60);
            timeTip="已使用"+time+"分钟";

            titleColor = getTitleColor(time);
            buttonClass = "btn btn-info btn-sm";
            buttonValue = "退出";
        }




        var room="";

        var ifTimer="";
        if(time!=""){
            ifTimer = "timer='"+time+"'";
        }

        room+="                    <div id=\""+roomCode+"\" code = '"+code+"' "+ifTimer+" class=\"box col-md-3\">";
        room+="                        <div class=\"box-inner\">";
        room+="                            <div  class=\"box-header well\" style=\"background-color: "+titleColor+" ;\" data-original-title=\"\">";
        room+="                                <h2 style=\"color: #ffffff\"><i class=\"glyphicon glyphicon-user \"></i>";
        if(isSingle=="2")
        room+= "<i class=\"glyphicon glyphicon-user black\"></i>";
        room+="                                         "+code+"房间</h2>";

        room+="                                <h2 style=\"color: #ffffff;position: relative;float:right\">"+timeTip+"</h2>";
        room+="                            </div>";
        room+="                            <div class=\"box-content\">";
        room+="                                <div class=\"row\">";
        room+="                                    <div class=\"col-md-4\"><h6>"+(isSingle=="2"?'双缸':"单缸")+"</h6></div>";
        room+="                                    <div class=\"col-md-4\"><h6>20元</h6></div>";
        room+="                                    <div class=\"col-md-4\">";
        room+="                                        <button name='roombtn' data='"+roomCode+"' class=\""+buttonClass+"\">"+buttonValue+"</button>";
        room+="                                    </div>";
        room+="                                </div>";
        room+="                            </div>";
        room+="                        </div>";
        room+="                    </div>";
        return room;
    }


    var getTitleColor=function(minute){
        var color="";
        if(minute == -1){
            color = "#73a839";
        }else if(minute<=30){
            color = "#808080";
        }else if(minute <=50){
            color = "#dd5600";
        }else{
            color = "#c71c22";
        }
        return color;
    }

    var initRoom=function($id){
        $id.find(".box-header").css("background-color",getTitleColor(-1));
        $id.find("button").attr("class","btn btn-success btn-sm").html("使用");
        $id.find("h2").eq(1).html("");
    }

    //点击使用
    $(document).on("click","[name='roombtn']",function(){
        //获得DOM节点
         var $t=$(this);
        var id=$t.attr("data");
        var  $id=$("#"+id);

        if($t.html() == "使用"){
            //更改页面使用状态
            $id.find("h2").eq(1).html("已使用0分钟");
            var time=0;
            $id.find(".box-header").css("background-color",getTitleColor(time));
            $t.everyTime("60s",id,function(){
                time++;
                $id.find("h2").eq(1).html("已使用"+time+"分钟");
                $id.attr("timer",time);//记录当前DOM使用了多少时间

                $id.find(".box-header").css("background-color",getTitleColor(time));

            });
            //alert(
            $id.find("button").attr("class","btn btn-info btn-sm").html("退出");
            //);

            //设为使用中
            var code=$id.attr("code");
            $.ajax(url+"/updateRoom?roomCode="+code)
                .done(function(data){
                }).fail(function(error){
                });

        }else{
            //退出

            initRoom($id);//UI初始化
            var code=$id.attr("code");

            if(Number($id.attr("timer"))<=10 ){
                //当使用时间少于10分钟时，当作误操作处理，不计费

            }else{
                //开始计费


              var isSingle =  $id.find(".row").find(".col-md-4:eq(0)").find("h6").html();
                var price = "0";
                if(isSingle == "单缸"){
                    price = window.single1;
                }else{
                    price = window.single2;
                }
                $.ajax(url+"/saveIncome?money="+price+"&type=4&remark="+code)
                    .done(function(data){
                    }).fail(function(error){
                    });
            }
            $.ajax(url+"/removeRoom?roomCode="+code)
                .done(function(data){
                }).fail(function(error){
                });
        }





    });










});
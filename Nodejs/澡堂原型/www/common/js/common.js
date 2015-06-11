/**
 * Created with JetBrains WebStorm.
 * User: lucy
 * Date: 15-4-27
 * Time: 下午10:37
 * To change this template use File | Settings | File Templates.
 */

//窗口resize的时候
$(window).resize(function(){
    $("#shopBanner").each(function(){
        $("#shopBanner").css("height",$(this).width()/3*1);
    });
});

$(function () {
    $("#shopBanner").css("height",$(this).width()/3*1);
});



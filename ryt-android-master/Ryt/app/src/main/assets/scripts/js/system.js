/**
 * Created by Administrator on 2016/7/21.
 */

//页面切换
function sliderTabs(){
    var oMenu=$('.swiper-menu span,.com-nav .swiper-menu .item .line');
    var swiper1=new Swiper('.swiper1',{
        speed:500,
        onSlideChangeStart:function(){
            oMenu.eq(swiper1.activeIndex).addClass('active').siblings('span').removeClass('active');
        }
    });
}

$(function () {
    var oMenu=$('.swiper-menu span');
    var swiper1=new Swiper('.swiper1',{
        speed:500,
        autoHeight: true,
        onSlideChangeStart:function () {
            oMenu.eq(swiper1.activeIndex).addClass('active').siblings('span').removeClass('active');
        }
    });


    oMenu.click(function () {
        var index=$(this).index();
        $(this).addClass('active').siblings('span').removeClass('active');
        swiper1.slideTo(index);
    });

});



//融资详情
(function () {
    $('.details-rz .project .users dt .button').click(function () {
        $(this).toggleClass('active');
        $(this).find('.heart').toggleClass('empty');
    })
})();
//主页限制字数
(function(){
    function txtT(id,len){
        var Id = $(id)
        Id.each(function(){
            var txt = $(this).text().length;//获取当前容器的字符数
            if(txt>len){
                $(this).text($(this).text().substr(0,len)).append("...")
            }
        })
    }
    txtT(".lh-list li .h-txt p",18)//艺术家主页，信息内容限制
    txtT(".formerly li .img-works .txt-works p",20)//投过的，信息内容限制
})();
//拍卖折叠
(function(){
    var act = $(".flow-btn"),
        max = act.find(".flow-ct");
    max.each(function(){
        $(this).find(".fl-page").click(function(){
            var box = $(this).siblings(".fl-box");
             if(box.hasClass("hide")){
             $(this).find(".f-down").css({
             webkitTransform:"rotate(-134deg)",
             marginTop:-3
             })
             box.removeClass("hide");
             box.addClass("show");
             //初始化
             $(this).css({
             borderBottom:0,
             height:41
             });
             box.removeClass("fb-bd");
             box.find(".fl-pd1").removeClass("bor-top-eb");
             box.find(".fl-pd2").removeClass("fb-bd");
             }else{
             $(this).find(".f-down").css({
             webkitTransform:"rotate(45deg)",
             marginTop:-7
             })
             box.addClass("hide");
             box.removeClass("show");
             $(this).css({
             borderBottomWidth:1,
             borderBottomColor:"#ededed",
             borderStyle:"solid",
             borderRightWidth:0,
             borderLeftWidth:0,
             height:42
             });
             box.addClass("fb-bd");
             box.find(".fl-pd1").addClass("bor-top-eb");
             box.find(".fl-pd2").addClass("fb-bd");
             }
            sliderTabs()
        })

    })
})();


(function(){
    var parentDiv = $("#bottomButton")
    // var box = $(".btm-min"),
    //     btn = box.find(".btm-fixed"),
    //     PaGe = box.find(".btm-box"),
    //     Pbg = PaGe.find(".btm-bg"),
    //     Ppg = PaGe.find(".btm-b-page");

    parentDiv.on("click", ".bd-btn-cell", function () {
        var box = $(".btm-min"),
            PaGe = box.find(".btm-box"),
            Pbg = PaGe.find(".btm-bg"),
            Ppg = PaGe.find(".btm-b-page");
        Pbg.show()
        Ppg.css({
            Transform: "translateY(0)"
        })
    })
    parentDiv.on("click", ".btm-bg", function () {
        var box = $(".btm-min"),
            PaGe = box.find(".btm-box"),
            Pbg = PaGe.find(".btm-bg"),
            Ppg = PaGe.find(".btm-b-page");
        Pbg.hide()
        Ppg.css({
            Transform: "translateY(100%)"
        })
    })
    // Pbg.on("click", function () {
    //     Pbg.hide()
    //     Ppg.css({
    //         Transform: "translateY(100%)"
    //     })
    // })

    parentDiv.on("click", ".deal-icon", function () {
        if ($(this).hasClass("mo")) {
            $(this).addClass("act")
            $(this).removeClass("mo")
        } else {
            $(this).removeClass("act")
            $(this).addClass("mo")
        }
    })
    //
    // Ppg.find(".deal").find(".deal-icon").on("click", function () {
    //     if ($(this).hasClass("mo")) {
    //         $(this).addClass("act")
    //         $(this).removeClass("mo")
    //     } else {
    //         $(this).removeClass("act")
    //         $(this).addClass("mo")
    //     }
    //
    // })
})()

$(document).ready(function () {
    $(".list-group-item.first").click(function () {
        console.log("hallo, my friend");
    });
    $(".menu-sild").height($(window).height() - $(".navbar").height());
    $(".navbar-nav li a").click(function(){
        $(this).addClass("zx").parents().siblings().find("a").removeClass("zx");
    })
});
$(".loginBtn").click(function () {
    if ($("#username").val() == "" || $("#username").val() == undefined) {
        tipinfo("用户名不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/login/login",
        data: {"userName": $("#username").val(),"password":$("#password").val()},
        dataType: "JSON",
        success: function (data) {
            if (data.result == "OK") {
                location.href = "/page";
            } else {
                tipinfo(data.message);
            }
        }
    })
});
/*$("body").keydown(function () {
    if (event.keyCode == "13") {//keyCode=13是回车键
        $(".loginBtn").click();
    }
});*/
document.onkeydown = function (event) {
    e = event ? event : (window.event ? window.event : null);
    if (e.keyCode == 13) {
        //执行的方法
        $(".loginBtn").click();
    }
}
$('.lookPwd').click(function () {
    if($(this).prev().attr("type")=='password'){
        $(this).prev().prop("type","text");
        $(this).find('img').prop("src","../img/yes.png");
    }else{
        $(this).prev().prop("type","password");
        $(this).find('img').prop("src","../img/no.png");
    }
});

/*修改学生页面*/
$("#editStudentScore").click(function () {
    var attendRateVal =  $("input[name='attendRate']").val();
    if(attendRateVal == null || attendRateVal == undefined || attendRateVal == ""){
        tipinfo("出勤率不能为空");
        return ;
    }
    var attendRate = Number($("input[name='attendRate']").val());
    if(attendRate > 100){
        tipinfo("出勤率不得大于100");
        return ;
    }else if ( attendRate <0 ){
        tipinfo("出勤率不得为负数");
        return ;
    }
    // var studentScore = $("#studentScore").serialize();
    var studentScore = {
        studentCode:$('input[name="studentCode"]').val(),
        projectId:$('input[name="projectId"]').val(),
        attendRate:$('input[name="attendRate"]').val()
    }
    var courseNames = [];
    var scores = [];
    var types = [];
    var flg = 1;
    $("input[name='courseName']").each(
        function () {
            var v = $(this).val();
            if (v == undefined || v == "" || v == null) {
                tipinfo("课程名称不能为空!");
                flg = 2;
            }
            if (courseNames.indexOf(v) == -1) {
                courseNames.push(v);
            } else {
                tipinfo("课程名字不能相同!");
                flg = 2;
            }
        });
    if (flg != 1) {
        return;
    }
    $("input[name='score']").each(
        function () {
            var v = $(this).val();
            var num = Number($(this).val());
            // flg = checkNumber($(this).val());
            // if(!flg){
            //     flg = 2;
            //     tipinfo("输入的数据不合法");
            //     return ;
            // }
            if (num > 100) {
                tipinfo("分数最大不能大于100",$(this));
                flg = 2;
                return false;
            }else if (num <0){
                tipinfo("分数不能为负数",$(this));
                flg = 2;
                return false;
            }
            scores.push(Number(v).toFixed(2));
        });
    if (flg != 1) {
        return;
    }
    $("select[name='type']").each(
        function () {
            var v = $(this).val();
            var num = $(this).parent().prev().find("input").val();
            if(num != ''){
                var vb = tongbu(num);
                if(v != vb){
                    tipinfo("分数不在区间内",$(this).parent().prev().find("input"));
                    flg = 2;
                }
            }
            types.push(v);
        });
    if (flg != 1) {
        return;
    }
    // var datalist = {courseName: courseNames.toString(), score: scores.toString(), type: types.toString()};
    studentScore.courseName=courseNames.toString();
    studentScore.score = scores.toString();
    studentScore.type = types.toString();
    $.ajax({
        type: "POST",
        url: "/student/updateClassScore",
        data: studentScore,
        dataType: "JSON",
        success: function (data) {
            if (data.result == "OK") {
                tipinfo("修改成功");
                setTimeout(function () {
                    closewindow(1)
                }, 800);
                return;
            }
            tipinfo(data.message);
        }
    })
});
function selectOnChange(){
    $("select[name='type']").change(
        function () {
            var num = $(this).parent().prev().find("input").val("");
        });
}
var htmlselefirst = "<tr>\n" +
    "<td><input type=\"text\" name=\"courseName\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
    "    <td><input type=\"text\" onBlur=\"panduan()\" name=\"score\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
    "    <td>\n" +
    "    <select name=\"type\" style=\"float:left;margin:0 auto;width: 70px;height: 30px\">\n";
var htmlselelast ="    </select>\n" +
    "    </td>\n" +
    "    <td>\n" +
    "    <a class=\"joysupply-txt-color-on\" onclick=\"addTR()\">添加</a>\n" +
    "    <a class=\"joysupply-txt-color-on joysupply-txt-color-error\" onclick=\"delTR(this)\">删除</a>\n" +
    "    </td>\n" +
    "    </tr>";
$("#closeWindow").click(function () {
    closewindow();
});
layui.use(["form", "table"], function () {
    var form = layui.form
        , table = layui.table;
    window.addTR = function () {
        var html = htmlselefirst+selectFive()+htmlselelast;
        $('#curriculumType').find("tbody").append(html);
        form.render();
    }
    window.delTR = function (obj) {
        obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
    }

});
$(function(){
    selectOnChange();
})
function panduan(){
    $("input[name='score']").each( function () {
            var v = $(this).val();
            var num = Number($(this).val());
            if (num > 100) {
                tipinfo("分数最大不能大于100",$(this));
                return false;
            }else if (num <0){
                tipinfo("分数不能为负数",$(this));
                return false;
            }else{
                if(v != ''){
                    var code = tongbu(num);
                    if(code!=""){
                        $(this).parent().next().find("select").find("option[value = '"+code+"']").attr("selected","selected").siblings().removeAttr("selected");
                    }
                }
            }
        });
}
function tongbu(num){
    var f = checkNumber(num);
    if(!f){
        tipinfo("输入的数据不合法");
        return "";
    }
    num = Number(num).toFixed(0);
    var fivePointCode = "";
    for ( var i = 0 ; i < fivePint.length ; i ++ ){
        var v = fivePint[i].section.split("-");
        if( num >= Number(v[0]) && num <= Number(v[1]) ){
            fivePointCode = fivePint[i].fivePointCode;
            break;
        }
    }
    return fivePointCode;
}
function selectFive(){
    var result="";
    for ( var i = 0 ; i < fivePint.length ; i ++ ){
        result+="<option value="+ fivePint[i].fivePointCode +">"+ fivePint[i].pointName +"</option>";
    }
    return result;
}
function checkNumber(theObj) {
    var reg = /^[0-9]+.?[0-9]*$/;
    if (reg.test(theObj)) {
        return true;
    }
    return false;
}
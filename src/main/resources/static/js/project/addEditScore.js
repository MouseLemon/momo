/*添加学生页面*/
$("#addEditStudentScore").click(function () {
    if(fiveTempId == undefined || fiveTempId == "" || fiveTempId == null){
        tipinfo("改项目没有项目模板");
        return ;
    }
    var student = $("select[name='studentCode']").val();
    if(student == null || student == undefined || student == ""){
        tipinfo("学员姓名不能为空");
        return ;
    }
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
        studentCode:$('select[name="studentCode"]').val(),
        projectId:$('select[name="projectId"]').val(),
        attendRate:$('input[name="attendRate"]').val()
    }
    var courseNames = [];
    var scores = [];
    var types = [];
    var flg = 1;
    $("input[name='courseName']").each(
        function () {
            var v = $(this).val();
            if (v == undefined || v == ""|| v==null) {
                tipinfo("课程名称不能为空");
                flg = 2;
                return false;
            }
            if (courseNames.indexOf(v) == -1) {
                courseNames.push(v);
            } else {
                tipinfo("课程名字不能相同");
                flg = 2;
                return false;
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
            //     return false;
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
        url: "/student/saveClassScore",
        data: studentScore,
        dataType: "JSON",
        success: function (data) {
            if (data.result == "OK") {
                tipinfo("添加成功");
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
$("#editStudentScore").click(function () {
    var studentScore = $("#studentScore").serialize();
    var courseNames = [];
    var scores = [];
    var types = [];
    var flg = 1;
    $("input[name='courseName']").each(
        function () {
            var v = $(this).val();
            if (v == undefined || v == "") {
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
            if (v == undefined || v == "") {
                tipinfo("得分不能为空");
                flg = 2;
            }
            scores.push(v);
        });
    if (flg != 1) {
        return;
    }
    $("select[name='type']").each(
        function () {
            types.push($(this).val());
        });
    // var datalist = {courseName: courseNames.toString(), score: scores.toString(), type: types.toString()};
    studentScore.courseName=courseNames.toString();
    studentScore.score = scores.toString();
    studentScore.type = types.toString();
    var data = studentScore;
    $.ajax({
        type: "POST",
        url: "/student/saveClassScore",
        data: data,
        dataType: "JSON",
        success: function (data) {
            if (data.result == "OK") {
                tipinfo("添加成功");
                setTimeout(function () {
                    closewindow(1)
                }, 800);
                return;
            }
            tipinfo(data.message);
        }
    })
});
$("#closeWindow").click(function () {
    closewindow();
});
layui.use(["form", "table"], function () {
    var form = layui.form
        , table = layui.table;
    window.addTR = function () {
        var html = htmlselefirst+selectFive()+htmlselelast;
        $('#curriculumType').find("tbody").append(html);
        selectOnChange();
        form.render();
    }
    window.delTR = function (obj) {
        obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
    }
    form.on('select(projectId)', function (data) {
        var projectCode = data.value;
        var tempCode = $(data.elem).find("option:selected").attr("tempCode");
        if(tempCode == undefined || tempCode == "" || tempCode == null){
            tipinfo("该项目没有成绩模板");
            $('#curriculumType').find("tbody").html("");
        }else {
            fiveTempId = tempCode;
            fiveTempCode(tempCode);
            projectType(projectCode);
            obtainStudent(projectCode);
            projectType(projectCode);
        }
    });
    form.on('select(studentCode)', function (data) {
        var serial = data.elem[data.elem.selectedIndex].title;
        $("#serial").val(serial);
    });
    $(function () {
        var projectCode = $("select[name='projectId']").val();
        var tempCode = $("select[name='projectId']").find("option").attr("tempCode");
        if (tempCode != undefined && tempCode != ""){
            fiveTempId = tempCode;
            fiveTempCode(tempCode);
            projectType(projectCode);
        }else {
            tipinfo("该项目没有成绩模板");
        }
        //获取学生列表
        obtainStudent(projectCode);
    });
    
    function fiveTempCode(tempCode) {
        //查询项目对应的模板
        $.ajax({
            type: "GET",
            url: "/student/queryScoreListNoPage",
            data: {parentCode: tempCode},
            async:false,
            dataType: "JSON",
            success: function (data) {
                fivePint = data.data;
            }
        })
        
    }

    function obtainStudent(projectCode) {
        $.ajax({
            type: "GET",
            url: "/student/queryProjectStudentByProCode",
            data: {projectId: projectCode},
            async:false,
            dataType: "JSON",
            success: function (data) {
                var datalist = data.data;
                var students = "<option value=\"\">请选择学员</option>";
                for (var i = 0; i < datalist.length; i++) {
                    students += "<option title='" + datalist[i].serial + "' "+((datalist[i].ishave == 1)?"disabled":"")+" value=\"" + datalist[i].studentCode + "\">" + datalist[i].name + "</option>";
                }
                $("select[name='studentCode']").html(students);
                form.render('select');
            }
        })
    }

    function projectType(projectCode) {
        $.ajax({
            type: "GET",
            url: "/student/queryClassScoreCourseNameByProjectId",
            data: {projectId: projectCode},
            async:false,
            dataType: "JSON",
            success: function (data) {
                var datalist = data.data;
                var fivePints = selectFive();
                if (datalist.length < 1) {
                    var htmlfist = "<tr>\n" +
                        "            <td><input type=\"text\" name=\"courseName\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
                        "            <td><input type=\"number\" onBlur=\"panduan()\" name=\"score\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
                        "            <td>\n" +
                        "                <select name=\"type\" style=\"float:left;margin:0 auto;width: 70px;height: 30px\">\n" +fivePints+
                        "                </select>\n" +
                        "            </td>\n" +
                        "            <td>\n" +
                        "                <a class=\"joysupply-txt-color-on\" onclick=\"addTR()\">添加</a>\n" +
                        "            </td>\n" +
                        "        </tr>";
                    $('#curriculumType').find("tbody").html(htmlfist);
                } else if (datalist.length > 0) {
                    var tbody = "";
                    if (datalist.length == 1) {
                        tbody += "<tr>\n" +
                        "            <td><input type=\"text\" name=\"courseName\" autocomplete=\"off\" class=\"layui-input\" value='"+datalist[0]+"'/></td>\n" +
                        "            <td><input type=\"number\" onBlur=\"panduan()\" name=\"score\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
                        "            <td>\n" +
                        "                <select name=\"type\" style=\"float:left;margin:0 auto;width: 70px;height: 30px\">\n" + fivePints+
                        "                </select>\n" +
                        "            </td>\n" +
                        "            <td>\n" +
                        "                <a class=\"joysupply-txt-color-on\" onclick=\"addTR()\">添加</a>\n" +
                        "            </td>\n" +
                        "        </tr>";
                    } else {
                        for (var i = 0; i < datalist.length; i++) {
                            if (i == 0) {
                                tbody += "<tr>\n" +
                                    "            <td><input type=\"text\" name=\"courseName\" autocomplete=\"off\" class=\"layui-input\" value='"+datalist[i]+"'/></td>\n" +
                                    "            <td><input type=\"number\" onBlur=\"panduan()\" name=\"score\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
                                    "            <td>\n" +
                                    "                <select name=\"type\" style=\"float:left;margin:0 auto;width: 70px;height: 30px\">\n" +fivePints+
                                    "                </select>\n" +
                                    "            </td>\n" +
                                    "            <td>\n" +
                                    "                <a class=\"joysupply-txt-color-on\" onclick=\"addTR()\">添加</a>\n" +
                                    "            </td>\n" +
                                    "        </tr>";
                            } else {
                                tbody += "<tr>\n" +
                                    "            <td><input type=\"text\" name=\"courseName\" autocomplete=\"off\" class=\"layui-input\" value='"+datalist[i]+"' /></td>\n" +
                                    "            <td><input type=\"number\" onBlur=\"panduan()\" name=\"score\" autocomplete=\"off\" class=\"layui-input\"/></td>\n" +
                                    "            <td>\n" +
                                    "                <select name=\"type\" style=\"float:left;margin:0 auto;width: 70px;height: 30px\">\n" +fivePints+
                                    "                </select>\n" +
                                    "            </td>\n" +
                                    "            <td>\n" +
                                    "                <a class=\"joysupply-txt-color-on\" onclick=\"addTR()\">添加</a>\n" +
                                    "                <a class=\"joysupply-txt-color-on joysupply-txt-color-error\" onclick=\"delTR(this)\">删除</a>\n" +
                                    "            </td>\n" +
                                    "        </tr>";
                            }
                        }
                    }
                    $('#curriculumType').find("tbody").html(tbody);
                }
                form.render();
                selectOnChange();
            }
        })
    }
});
function panduan(){
    $("input[name='score']").each(
        function () {
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
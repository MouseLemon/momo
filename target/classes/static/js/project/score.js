/*学生管理页面*/
var count = 0;
var dataParam = {};
var courseNames;
var flg = "百分制";
var table;
var studentCode = '';
var projectId = '';
var all = '';
layui.use(["form", "table", "jquery", 'laypage', 'laydate','element','layer','upload'], function () {
    table = layui.table;
    element = layui.element;
    var form = layui.form;
    var laypage = layui.laypage;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var upload = layui.upload;
    //日历
    laydate.render({
        elem: '#test1'
    });
    laydate.render({
        elem: '#test2'
    });
    form.on('radio(nameType)', function (data) {
        if(count != 0){
            upfenzhi();
        }
    });

    //批量上传
    var uploadInst = upload.render({
        elem: '#import' //绑定元素
        , url: '/student/importStudentScore' //上传接口
        , accept: 'file'
        , done: function (res) {
            //上传完毕回调
            if (res.result == 'OK') {
                layer.msg("导入成功")
                reload();
            } else {
                reload();
                layer.alert(res.message, {icon: 2}, function (index) {
                    location.href = '/teacher/exportMsg?key=importStudentScoreErrorMsg';
                    layer.close(index);
                })
            }

        }
        , error: function () {
            layer.msg("导入异常")
        }
    });
    
    //添加学员
    $("#searchStudents").click(function () {
        openwindow("/student/addEditScore", "新建成绩", "800", "360", false, reload);
    });
    //搜索
    $('#search').click(function () { //搜索，重置表格
        queryParam();
        tabledata(dataParam);
        laypageInt();
        if (count < 1) {
            $("#tablePage").hide();
        } else {
            $("#tablePage").show();
        }
    })

    table.on('checkbox(scoreTable)', function(obj){
        var data = obj.data;
        var type=obj.type;
        var checked=obj.checked;
        if(type == 'all') {
            all = "all"
            return false;
        }else {
            all = '';
            studentCode = obj.data.studentCode.split(",")[0];
            projectId = obj.data.studentCode.split(",")[1];
        }
    });

    //更改数据百分五分
    function upfenzhi() {
        var param = 1;
        var t = $(".layui-form-radioed").find("div").text();
        if (t == "评级") {
            if (flg == "评级") {
                return;
            }
            flg = "评级";
            param = 2;
        } else if (t == "百分制") {
            if (flg == "百分制") {
                return;
            }
            flg = "百分制";
            param = 1;
        }
        for (var i = 0; i < courseNames.length; i++) {
            $("td[data-field='courseName" + i + "']").each(
                function () {
                    if (param == 1) {
                        // var num = Number($(this).find("div").text()) * 20;
                        // if (num != 0) {
                        //     $(this).find("div").html(num.toFixed(2));
                        // }
                        $(".courseHide").removeClass("layui-hide");
                        $(".fivePintHide").addClass("layui-hide");
                    } else {
                        // var num = Number($(this).find("div").text()) / 20;
                        // if (num != 0 && false) {
                        //     $(this).find("div").html(num.toFixed(2));
                        // }
                        $(".courseHide").addClass("layui-hide");
                        $(".fivePintHide").removeClass("layui-hide");
                    }

                })
        }
    }

    window.tabledata = function (data) {
        flg = "百分制";
        loading(true);
        if (data.page == null || data.page == undefined) {
            data.page = 1;
        }
        if (data.limit == null || data.page == undefined) {
            data.limit = 10;
        }
        var th01 = "<tr>\n" +
            "<th lay-data=\"{field:'studentCode',type:'checkbox', fixed: 'left'}\"></th>"+
            "<th lay-data=\"{field:'studentName', fixed: 'left',width:120}\">姓名</th>" +
            "<th lay-data=\"{field:'serial', fixed: 'left',width:120,align: 'center'}\">学号</th>" +
            "<th lay-data=\"{field:'sex', fixed: 'left',width:90,align: 'center'}\">性别</th>" +
            "<th lay-data=\"{field:'telephone', fixed: 'left',width:120,align: 'center'}\">联系电话</th>" +
            "<th lay-data=\"{field:'projectName', fixed: 'left',width:120,align: 'center'}\">项目名称</th>" +
            "<th lay-data=\"{field:'attendRate', fixed: 'left',width:90,align: 'center'}\">出勤率</th>";
        var th02 = "<th lay-data=\"{field:'edis', fixed: 'right',align: 'center',width:96}\">操作</th>" + "</tr>";
        $.ajax({
            type: "GET",
            url: "/student/queryPageScore",
            dataType: "JSON",
            async: false,
            data: data,
            success: function (data) {
                courseNames = data.data;
                if(courseNames == null || courseNames == "" ){
                    $("#scoreTable").find("thead").html("");
                    table.init('scoreTable', {});
                    $('.layui-table-header').hide();
                    count = 0;
                    return ;
                }
                $(".layui-table-header").show();
                var widthtd = ($('#scoreTable').width() - 726) / courseNames.length;
                var thHtml = th01;
                if (data.data != null && data.data != "") {
                    for (var i = 0; i < courseNames.length; i++) {
                        thHtml += "<th lay-data=\"{field:'" + "courseName" + i + "', width:" + widthtd + ",align: 'center'}\">" + courseNames[i] + "</th>";
                        // thHtml += "<th lay-data=\"{field:'" + "courseName" + i + "'}\">" + courseNames[i] + "</th>\n";
                    }
                }
                thHtml += th02;
                $("#scoreTable").find("thead").html(thHtml);
            }
        });
        $.ajax({
            type: "GET",
            url: "/student/queryClassScore",
            dataType: "JSON",
            async: false,
            data: data,
            success: function (data) {
                count = data.count;
                var courseDatas = data.data;
                var trHtml = "";
                if (data.data != null && data.data != "") {
                    for (var i = 0; i < courseDatas.length; i++) {
                        trHtml += "<tr><td> <input type='hidden' name='proStuCode' value='"+courseDatas[i].studentCode+","+courseDatas[i].projectCode+"' /></td>";
                        trHtml += "<td>" + courseDatas[i].studentName + "</td>";
                        trHtml += "<td>" + courseDatas[i].serial + "</td>";
                        if (courseDatas[i].sex == 1) {
                            trHtml += "<td>男</td>\n";
                        } else {
                            trHtml += "<td>女</td>\n";
                        }
                        trHtml += "<td>" + courseDatas[i].telephone + "</td>";
                        trHtml += "<td>" + courseDatas[i].projectName + "</td>";
                        trHtml += "<td>" + courseDatas[i].attendRate + "%</td>";
                        for (var j = 0; j < courseDatas[i].course.length; j++) {
                            var chengji = Number(courseDatas[i].course[j])==0?"-":Number(courseDatas[i].course[j]);
                            trHtml += "<td class=\"testtd\">" +
                                "<span class='courseHide'>"+chengji+"</span>"+
                                "<span class='fivePintHide layui-hide'>"+(courseDatas[i].fivePints[j]=="null"?"-":courseDatas[i].fivePints[j])+"</span>"+
                                "</td>";
                        }
                        trHtml += "<td>" +
                            "<a class=\"joysupply-txt-color-on\"  onclick=\"edit('" + courseDatas[i].studentCode + "','" + courseDatas[i].projectCode + "','" + courseDatas[i].tempCode + "')\"" +
                            " th:each=\"func:${session.funcList}\" th:if=\"${func == 'cjgl_edit'}\">编辑</a>" +
                            "<a class=\"joysupply-txt-color-error joysupply-txt-color-on\" onclick=\"del('" + courseDatas[i].studentCode + "','" + courseDatas[i].projectCode + "')\" style='margin-left:5px'" +
                            " th:each=\"func:${session.funcList}\" th:if=\"${func == 'cjgl_del'}\">删除</a>" +
                            "<td>";
                        trHtml += "</tr>";
                    }
                }
                $("#scoreTable").find("tbody").html(trHtml);
                $('td.testtd').find("div").removeAttr('class');
                loading(false);
            }
        });
        table.init('scoreTable', {});
        upfenzhi();
    }

    $(function () {
        tabledata(dataParam);
        laypageInt();
        if (count < 1) {
            $("#tablePage").remove();
        }
    })

    function studentDel(studentCode) {
        if (studentCode == null || studentCode == undefined) {
            tipinfo("操作失败");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/student/deleteStudentByCode",
            dataType: "JSON",
            data: {studentCode: studentCode},
            success: function (data) {
                reload();
            }
        })
    }

    function studentStatus(studentCode, status) {
        var msg = "确定要禁用该学员吗";
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
            msg = "确定要启用该学员吗";
        }
        confirm(msg, function () {
            $.ajax({
                type: "POST",
                url: "/student/updateStudentStatus",
                dataType: "JSON",
                data: {studentCode: studentCode, status: status},
                success: function (data) {
                    reload();
                }
            })
        });
    }

    function laypageInt() {
        laypage.render({
            elem: 'tablePage',
            count: count,
            prev: '<em class="layui-icon" style="margin-right: 0px">&#xe603;</em>',
            next: '<em class="layui-icon" style="margin-right: 0px">&#xe602;</em>',
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function (obj) {
                var page = obj.curr;
                var limit = obj.limit;
                dataParam.page = page;
                dataParam.limit = limit;
                tabledata(dataParam);
            }
        });
    }
});
// 打印证书，成绩单
function printTemplate(type) {
    var arr = [];
    $(".layui-form-checked").each(function () {
        var v = $(this).parent().parent().parent().attr("data-index");
        if(v != undefined && arr.indexOf(v) == -1){
            arr.push(v);
        }
    })
    var pId = [];
    var sId = [];
    for ( var i = 0 ; i < arr.length ; i++ ){
        var spId = $('input[name="proStuCode"]').eq(arr[i]).val();
        sId.push(spId.split(",")[0]);
        pId.push(spId.split(",")[1]);
    }
    if(pId.length<1) {
        layer.msg('请选择学生');
        return false;
    }
    // 选择打印模板
    layer.open({
        type: 2,
        title: '选择打印模板',
        area: ['500px', '300px'],
        shade: 0.3,
        offset: 'auto',
        content: '/scoreTemplateController/chooseTemplatePage?type=' + type,
        btn: ['确定', '取消'],
        btnAlign: 'c',
        yes: function (index, layero) {
            //do something
            var body = layer.getChildFrame('body', index);
            var iframeWin = window[layero.find('iframe')[0]['name']];
            var templateId = iframeWin.getTemplateId();
            if(templateId == undefined || templateId == '') {
                layer.msg("请选择模板");
                return false;
            }
            layer.close(index);
            //查询打印内容
            var printTemplate = "";
            var tital = "";
            $.post("/scoreTemplateController/getPrint",{studentCode:sId.join(','),projectId:pId.join(','),templateId:templateId},function (data) {
                if(data.success) {
                    tital = "打印证书";
                    if(type == 1) {
                        tital = "打印成绩证明";
                    }
                    printTemplate = data.printTemplate;
                    printHtml(tital,printTemplate);
                }else {
                    layer.msg("未设置默认模板")
                }
            })
        }
    });


}

function printHtml(tital,printTemplate) {
    var wind =window.open("",tital);
    wind.document.body.innerHTML = printTemplate;
    var table = wind.document.getElementsByTagName("table");
    if(table.length > 0) {
        for(var i = 0; i < table.length; i++)
        table[i].setAttribute("style","text-align:center");
    }
    wind.print();
    wind.close();
    return false;
}
//页面重新加载
var reload = function () {
    tabledata(dataParam);
}

function edit(stuCode, proCode , tempCode) {
    openwindow("/student/editScore?projectId=" + proCode + "&studentCode=" + stuCode+"&parentCode="+tempCode, "修改成绩", "800", "350", false, reload);
}

function del(stuCode, proCode) {
    var data = {projectId: proCode, studentCode: stuCode};
    confirm("确定要删除学员成绩吗？", function () {
        delAjax(data);
    });
}

//获取模糊多条件查询的参数
function queryParam() {
    var studentName = $("input[name='studentName']").val();
    var serial = $("input[name='serial']").val();
    var projectName = $("input[name='projectName']").val();
    var startTime = $("input[name='startTime']").val();
    var endTime = $("input[name='endTime']").val();
    dataParam = {
        studentName: studentName
        , serial: serial
        , projectName: projectName
        , startTime: startTime
        , endTime: endTime
    };
}

function delAjax(data) {
    $.ajax({
        type: "POST",
        url: "/student/deleteClassScoreByProClaCode",
        dataType: "JSON",
        data: data,
        success: function (data) {
            if (data.result) {
                tipinfo("删除成功");
            } else {
                tipinfo("删除失败");
            }
            reload();
        }
    })
}
//导出模板
$('#export').click(function () {
    location.href="/student/scoreTemplateDown";
})
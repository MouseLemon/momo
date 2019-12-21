/*学生管理页面*/
var table;
layui.use(["form", "table", "jquery","element",'upload'], function () {
    table = layui.table;
    element = layui.element;
    var form = layui.form;
    var upload = layui.upload;
    //添加学员
    $("#searchStudents").click(function () {
        openwindow("/student/addStudent", "新建学员", "600", "550", false, reload);
    });


    //学员列表
    var tableIns = table.render({
        elem: "#studentTable",
        id: "id",
        skin: 'line',
        url: "/student/queryStudentByParam",
        cols: [[
            {type: 'numbers', title: "序号", width: '10%', fixed: 'left'}
            , {field: 'name', title: "学员名称", width: '10%', fixed: 'left'}
            , {field: 'serial', title: "学号", width: '15%', align: 'center'}
            , {field: 'title', title: "性别", templet: "#studentSex", width: '6%', align: 'center'}
            , {field: 'idcard', title: "身份证号", width: '20%', align: 'center'}
            , {field: 'telephone', title: "联系电话", width: '15%', align: 'center'}
            , {field: 'project_name', title: "项目名称", width: '15%', align: 'center'}
            , {field: 'status', title: "状态", templet: "#statusTem", width: '6%', align: 'center'}
            , {field: 'create_time', title: "创建时间", width: '15%', align: 'center'}
            , {field: 'title', title: "操作", templet: "#operation", width: '15%', fixed: 'right', align: 'center'}
        ]],
        page: true,
        done: function () {
            //加载完之后执行方法
        }
    });
    //批量上传
    var uploadInst = upload.render({
        elem: '#addStudents' //绑定元素
        , url: '/student/importStudent' //上传接口
        , accept: 'file'
        , done: function (res) {
            //上传完毕回调
            if (res.result == 'OK') {
                layer.msg("导入成功")
                tableIns.reload({
                    page: {
                        curr: 1
                    }
                });
            } else {
                layer.alert(res.message, {icon: 2}, function (index) {
                    location.href = '/teacher/exportMsg?key=importStudentErrorMsg';
                    layer.close(index);
                })
            }

        }
        , error: function () {
            //请求异常回调
            layer.msg("导入异常")
        }
    });

    //模板下载
    $("#templateDown").click(function () {
        location.href = '/student/templateDown'
    });

    //搜索
    $('#search').click(function () { //搜索，重置表格
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                name: $("input[name='name']").val(),
                telephone: $("input[name='telephone']").val(),
                state: $("input[name='status']").val(),
                serial: $("input[name='serial']").val(),
                projectName: $("input[name='projectName']").val(),
                status: $("select[name='status']").val()
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    })
    //监听行工具事件
    table.on("tool(studentTable)", function (obj) {
        var data = obj.data;
        var studentCode = data.student_code;
        if (obj.event === 'updateStatus') {
            var status = data.status;
            studentStatus(studentCode, status);
        } else if (obj.event === 'stuEdit') {
            openwindow("/student/editStudent?studentCode=" + studentCode, "修改学员", "600", "580", false, reload);
        } else if (obj.event === 'stuDel') {
            confirm("确定要删除该学员吗", function () {
                studentDel(studentCode);
            });
        } else if (obj.event === 'stuUrl') {
            var url = data.url;
            if (url == null || url == undefined || url == "") {
                tipinfo("暂时没有图片");
                return;
            }
            var Img = new Image();
            Img.src = url;
            Img.onload = function () {

                var w = (document.documentElement.clientWidth || document.body.clientWidth) * 0.5;
                var h = document.documentElement.clientHeight || document.body.clientHeight;

                if (Img.width < w) w = Img.width;

//					if(Img.height>h)

                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: w + 'px',
                    skin: 'layui-layer-nobg', //娌℃湁鑳屾櫙鑹 
                    shadeClose: true,
                    offset: 'auto',
                    content: "<div style='text-align:center'><img src='" + Img.src + "'  style='width:100%' /></div>"
                });
            }
        }
    });

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

    //页面重新加载
    window.reload = function () {
        tableIns.reload({
            where: {
                name: $("input[name='name']").val(),
                telephone: $("input[name='telephone']").val(),
                state: $("input[name='status']").val(),
                serial: $("input[name='serial']").val(),
                projectName: $("input[name='projectName']").val(),
                status: $("select[name='status']").val()
            },
            page: {
                curr: 1
            }
        });
    }
});

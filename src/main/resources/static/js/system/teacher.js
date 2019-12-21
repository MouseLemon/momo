var tableIns;
var table;
layui.use(['laypage', 'layer', 'table', 'form', 'laydate', 'upload', 'element'], function () {
    element = layui.element;
    table = layui.table;
    var laypage = layui.laypage;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;
    var upload = layui.upload;


    var researchOffice = $('#researchOffice').val();
    // 绑定表格
    tableIns = table.render({
        id: 'id',
        elem: '#teacherInfo',
        url: '/teacher/listData',
        where: {
            researchOffice: researchOffice
        },
        skin: 'line',
        page: true, //开启分页
        limits: [10, 20, 30, 40, 50],
        cols: [
            [ //表头
                // {
                //     type: 'numbers',
                //     width: '5%',
                //     fixed: 'left'
                // },
                {
                    field: 'teacherName',
                    title: '教师姓名',
                    width: '9%',
                    align: 'center',
                    fixed: 'left'
                },
                // {
                //     field: 'userName',
                //     title: '用户名',
                //     align: 'center',
                //     width: '7%',
                //
                // },
                {
                    field: 'sex',
                    title: '性别',
                    align: 'center',
                    width: '6%',
                    templet: function (d) {
                        if (d.sex == 1) {
                            return '男';
                        } else if (d.sex == 2) {
                            return '女';
                        } else {

                            return null;
                        }

                    }
                },
                {
                    field: 'nationality',
                    title: '国籍',
                    width: '10%',
                    align: 'center'
                },

                {
                    field: 'teacherType',
                    title: '教师类别',
                    width: '15%',
                    align: 'center',
                },
                {
                    field: 'idCard',
                    title: '身份证号',
                    width: '20%',
                    align: 'center'
                },
                {
                    field: 'teleNum',
                    title: '电话号码',
                    width: '20%',
                    align: 'center'
                },
                {
                    field: 'researchOffice',
                    title: '教研室',
                    width: '10%',
                    align: 'center'
                },
                {
                    field: 'job',
                    title: '职称',
                    width: '10%',
                    align: 'center'
                },

                {
                    field: 'bankCode',
                    title: '银行卡号',
                    width: '20%',
                    align: 'center'
                },
                {
                    field: 'status',
                    title: '状态',
                    align: 'center',
                    width: '10%',
                    templet: function (d) {
                        if (d.status == 1) {
                            return '正常';
                        } else {
                            return '已停用';
                        }

                    }
                },
                {
                    field: 'createTime',
                    title: '新建时间',
                    width: '16%',
                    align: 'center'
                },
                {
                    field: 'updateTime',
                    title: '最后修改时间',
                    width: '16%',
                    align: 'center'
                },
                {
                    toolbar: '#barDemo',
                    title: '操作',
                    align: 'center',
                    width: '350',
                    fixed: 'right'
                }
            ]
        ]
    });

    // 搜索，重置表格
    $('.search').click(function () {
        tableIns.reload({
            where: {
                teacherName: $('input[name="teacherName"]').val(),
                telNum: $('input[name="telNum"]').val(),
                idCard: $('input[name="idCard"]').val(),
                teacherType: $('#teacherType').val(),
                status: $('#status').val(),
                teleNum: $('#teleNum').val(),
                nationality: $('#nationality').val(),
                researchOffice: researchOffice,
                r: Math.random()
            },
            page: {
                curr: 1
            }
        });
    });
    //修改状态
    form.on('switch(isEnable)', function (obj) {
        var personCode = obj.value;
        if (obj.elem.checked) {
            update(personCode, 1);
        } else {
            update(personCode, 0);
        }
    });

    var uploadInst = upload.render({
        elem: '#addAll' //绑定元素
        , url: '/teacher/importTeacher' //上传接口
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
                    location.href = '/teacher/exportMsg?key=teacherImportMsg';
                    layer.close(index);
                })
            }

        }
        , error: function () {
            //请求异常回调
            layer.msg("导入异常")
        }
    });
    // 新建
    $('.build').click(function () {

        layer.open({
            type: 2,
            title: '新建教师',
            area: ['1132px', '500px'],
            shade: 0.3,
            offset: 'auto',
            content: '/teacher/addTeacherInfo?researchOffice='+researchOffice,
            btnAlign: 'c',

            end: function () {
                var state = $("#state").val();
                if (state == '1') {
                    layer.msg('保存成功', {icon: 1})
                    tableIns.reload({
                        page: {
                            curr: 1
                        }
                    });
                    $("#state").val("");
                }
            }
        });

    });

    // 模板下载
    $('.templateDown').click(function () {
        location.href = '/teacher/templateDownload';
    });

    // 教师导出
    $('.teacherExport').click(function () {
        // $('<form method="post" action="' + '/teacher/teacherExport' + '">' +
        //     '<textarea name="data" >'+JSON.stringify(table.cache.id)+'</textarea>' +
        //     '</form>').appendTo('body').submit().remove();

        location.href = '/teacher/teacherExport?teacherName='+$('input[name="teacherName"]').val()+
        '&telNum='+$('input[name="telNum"]').val()+'&idCard='+$('input[name="idCard"]').val()+'&teacherType='+
            $('#teacherType').val()+'&status='+$('#teacherType').val()+'&teleNum='+$('#teleNum').val()+
        '&nationality='+$('#nationality').val();
    });

    // 监听工具条
    table.on('tool(equipment)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

        var data = obj.data; //获得当前行数据
        var teacherCode = data.teacherCode;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        if (layEvent === 'edit') {//修改
            layer.open({
                type: 2,
                title: '编辑教师',
                area: ['1132px', '500px'],
                shade: 0.3,
                offset: 'auto',
                content: '/teacher/addTeacherInfo?teacherCode=' + teacherCode,
                btnAlign: 'c',

                end: function () {
                    var state = $("#state").val();
                    if (state == '1') {
                        layer.msg('保存成功', {icon: 1})
                        tableIns.reload({
                            where: {
                                teacherName: $('input[name="teacherName"]').val(),
                                telNum: $('input[name="telNum"]').val(),
                                idCard: $('input[name="idCard"]').val(),
                                teacherType: $('#teacherType').val(),
                                status: $('#status').val(),
                                nationality: $('#nationality').val(),
                                r: Math.random()
                            }
                        });

                        $("#state").val("");
                    }

                }
            });
        } else if (layEvent === 'del') {//删除
            layer.confirm('是否要删除?', function (index) {
                var success = function (result) {
                    if (result.result == 'OK') {
                        layer.msg("删除成功", {icon: 1});
                        tableIns.reload({
                            where: {
                                teacherName: $('input[name="teacherName"]').val(),
                                telNum: $('input[name="telNum"]').val(),
                                idCard: $('input[name="idCard"]').val(),
                                teacherType: $('#teacherType').val(),
                                status: $('#status').val(),
                                nationality: $('#nationality').val(),
                                r: Math.random()
                            }
                        });

                    } else {
                        layer.msg(result.message, {
                            icon: 2
                        });
                    }
                }
                $.ajax({
                    type: "post",
                    url: "/teacher/deleteTeacher",
                    data: {teacherCode: teacherCode, r: Math.random()},
                    dataType: "json",
                    success: success,
                    error: function () {
                        layer.msg('未知错误', {icon: 2});
                    }
                })

            });
        } else if (layEvent === 'end') {//删除
            layer.confirm('是否要停用?', function (index) {
                updateStatus(2, teacherCode)

            });
        } else if (layEvent === 'start') {//删除
            layer.confirm('是否要启用?', function (index) {
                updateStatus(1, teacherCode)

            });
        } else if (layEvent === 'pic') {//照片预览
            layer.open({
                type: 2,
                title: '我的照片',
                area: ['500px', '400px'],
                shade: 0.3,
                offset: 'auto',
                content: '/teacher/myPic?teacherCode=' + teacherCode,
                btnAlign: 'c'

                /*	end: function() {
                          tableIns.reload({
                              page: {
                                  curr: 1
                              }
                          });
                    }*/
            });
        } else if (layEvent === 'resume') {//照片预览
            var resume = data.resume;
            if (resume == '') {
                layer.msg('请前去上传简历')
                return false;
            } else {
                // console.log(data);
                location.href = '/teacher/downloadResume?url=' + data.resume + '&resumeName=' + data.resumeName;
            }
        } else if (layEvent === 'create') {
            openwindow("/systemUser/addSystemUserPage?personCode=" + teacherCode+'&userName='+data.teleNum +'&password=a123123' + '&userType=13', "创建账户", "700", "250", false, reload);
        } else if (layEvent === 'update') {
            openwindow("/systemUser/updSystemUserPage?personCode=" + teacherCode, "账户信息", "700", "250", false, reload);
        } else if (layEvent === 'locAuth') {
            layer.open({
                type: 2,
                title: '设置楼群权限',
                area: ['800px', '650px'],
                shade: 0.3,
                offset: 'auto',
                content: '/teacher/setTeacherLocAuth?personCode=' + teacherCode,
                btn: ['确定', '取消'],
                btnAlign: 'c',
                yes: function (index, layero) {
                    //do something
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    var list = iframeWin.getList();
                    var buildingCodes = list.join(",")
                    $.post('/authority/insUpPersonBuildingAuth', {buildingCodes: buildingCodes, personCode: teacherCode}, function (data) {
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        if (data.result == 'OK') {
                            layer.msg("设置成功")
                        } else {
                            layer.msg("设置失败")
                        }

                    })

                }
            });

        } else if (layEvent === 'projectAuth') {

            //点击添加审核人之后弹出选择人的页面
            parent.layer.open({
                id: 'projectList',
                type: 2,
                title: '项目数据权限',
                area: ['65%', '65%'],
                content: '/teacher/setTeacherProjectAuth?personCode=' + teacherCode,
                btn: ['确定', '取消'],
                btnAlign: 'c',
                yes: function (index, layero) {
                    //do something
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    var list = iframeWin.getList();
                    console.log(list);
                    var projectIds = list.join(",")
                    $.post('/authority/insUpTeacherProjectAuth', {projectIds: projectIds, teacherCode: teacherCode}, function (data) {
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        if (data.result == 'OK') {
                            layer.msg("设置成功")
                        } else {
                            layer.msg("设置失败")
                        }

                    })

                }
            });


        }
    });


    //停用或启用
    function updateStatus(status, teacherCode) {
        var success = function (result) {
            if (result.result == 'OK') {

                layer.msg("操作成功", {icon: 1});
                tableIns.reload({
                    /*page: {
                        curr: 1
                    }*/
                    where: {
                        teacherName: $('input[name="teacherName"]').val(),
                        telNum: $('input[name="telNum"]').val(),
                        idCard: $('input[name="idCard"]').val(),
                        teacherType: $('#teacherType').val(),
                        status: $('#status').val(),
                        nationality: $('#nationality').val(),
                        r: Math.random()
                    }
                });

            } else {
                layer.alert(result.message, {
                    icon: 2
                });
            }
        }
        $.ajax({
            type: "post",
            url: "/teacher/updateTeacher",
            data: {status: status, teacherCode: teacherCode, r: Math.random()},
            dataType: "json",
            success: success,
            error: function () {
                layer.alert('未知错误', {icon: 2});
            }
        })
    }
});

function reload() {
    var params = {
        teacherName: $('input[name="teacherName"]').val(),
        telNum: $('input[name="telNum"]').val(),
        idCard: $('input[name="idCard"]').val(),
        teacherType: $('#teacherType').val(),
        status: $('#status').val(),
        nationality: $('#nationality').val(),
        r: Math.random()
    };
    tableIns.reload({
        where: params
    });
}

function update(personCode, isEnable) {
    var map = {"personCode": personCode, "isEnable": isEnable};
    $.ajax({
        type: "post",
        url: '/systemUser/updSystemUser',
        data: JSON.stringify(map),
        contentType: "application/json",
        success: function (data) {
            tableIns.reload({
                page: {
                    curr: 1
                }
            });
        },
        error: function () {
            tipinfo(data.message);
        }
    })
}
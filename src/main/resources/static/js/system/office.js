var table;
layui.use(['laypage', 'layer', 'table', 'form','laydate'], function(){
    table = layui.table;
    var laypage = layui.laypage;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    //执行一个laydate实例
    laydate.render({
        elem: '#startTime', //指定元素
    });
    laydate.render({
        elem: '#endTime', //指定元素
    });

    //执行一个laydate实例
    laydate.render({
        elem: '#dategv' //指定元素
    });

    // 绑定表格
    var tableIns = table.render({
        id: 'id',
        elem: '#officeListTable',
        cellMinWidth: 80,
        url: '/office/rewardCheckList',
        where: {},
        page: true, //开启分页
        limits: [10, 20, 30, 40, 50]
    });

    // 搜索，重置表格
    $('.search').click(function(){
        table.reload('officeList_id', {
            where: {
                year: $('input[name="year"]').val(),
                step: $('input[name="step"]').val(),
                section: $('input[name="section"]').val(),
                start_time: $('input[name="start_time"]').val(),
                end_time: $('input[name="end_time"]').val()
            }
        });
    });

    // 监听工具条
    table.on('tool(equipment)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var officeCode = data.officeCode;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        if(layEvent === 'edit') {//修改
            layer.open({
                type: 2,
                btnAlign:'c',
                title: ['用户信息', 'font-size:18px;'],
                // shadeClose: true,
                area: ['1000px', '700px'],
                content: '/office/editOffice?officeCode=' + officeCode,
                btn:['确定', '取消'],
                yes: function(index, layero){

                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    var r=iframeWin.submit();

                    if(r!=0){
                        var form = layer.getChildFrame('form', index);
                        var data = form.serialize();
                        $.ajax({
                            type: "post",
                            url: "/office/getOffice",
                            data: data,
                            dataType: "json",
                            success: function(data){

                                layer.close(index);
                                layer.msg('编辑成功', {icon: 1});
                                tableIns.reload({

                                    page: {
                                        curr: 1 //重新从第 1 页开始
                                    }
                                });
                            },
                            error: function(){
                                layer.msg('未知错误', {icon: 2});
                            }
                        })

                    }
                },
                success: function(layero, index) {
                    //layer.iframeAuto(index);
                    // layer.full(index);
                },
                end: function() { //销毁后触发
                    table.reload('officeList_id', {
                        height: 315
                        ,page: {
                            curr: 1
                        }
                    });
                }
            });
        }else if(layEvent === 'del') {//删除
            layer.confirm('是否要删除?', function(index) {
                var success = function(result) {
                    // console.log('删除：' + JSON.stringify(response));
                    // var result = JSON.parse(response);
                    if(result.success) {
                        layer.msg(result.msg);
                        table.reload('officeList_id', {
                            height: 315
                            ,page: {
                                curr: 1
                            }
                        });
                        // layer.close(index);

                    } else {
                        layer.alert(result.msg, {
                            icon: 2
                        });
                    }
                }
                var AjaxData = {
                    officeCode: officeCode,
                    r: Math.random()

                }
                ajax("/office/deleteOffice", AjaxData, success);

            });
        }
    });


});
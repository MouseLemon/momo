var laypage;
var form;
layui.use(['laydate', 'laypage', 'form'], function () {
    var laydate = layui.laydate;
    form = layui.form;
    laypage = layui.laypage;

    laydate.render({
        elem: '#startTime'
    });
    laydate.render({
        elem: '#endTime'
    });
    
    form.on('select(test)', function(data){
    	$("#baseFee").val("");
        $("#ksCount").val("");
        $("#personFactor").val("");
        $("#courseFactor").val("");
        $("#kcCount").html("");
    });
    form.on('select(test1)', function(data){
    	$("#baseFee").val("");
        $("#ksCount").val("");
        $("#personFactor").val("");
        $("#courseFactor").val("");
        $("#kcCount").html("");
    });
    
})
var vm = new Vue({
    el: "#app",
    data: {
        kcInfo: [],
        count: 0,
        limit: 2,
        serialNumber: '',
        startTime: '',
        endTime: '',
        flag: ''
    },
    watch: {
        flag: function () {
            $("#startTime").val(vm.startTime);
            $("#endTime").val(vm.endTime);
            $("#startTime").attr("disabled", "disabled");
            $("#endTime").attr("disabled", "disabled");
            $("#search").click();
        },
        count: function (count) {
            laypage.render({
                elem: 'jqPager', //元素id
                layout: ['prev', 'page', 'next', 'skip', 'count'],
                limit: vm.limit,
                count: count,
                curr: 1,
                jump: function (obj, first) {
                    currentPage = obj.curr;
                    limit = obj.limit;
                    if (!first) {
                        vm.queryPagingData(currentPage, limit);
                    }
                }
            });
        }
    },
    methods: {
        commit: function () {
            if (vm.kcInfo.count == 0 || vm.kcInfo.data.length == 0 ) {
                failureMsg("未发现需要保存的数据！");
                return false;
            }
            var data = vm.kcInfo.data;
            var upCode = data[0][0].upCode;
            this.saveKclist();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            if (startTime == "" || endTime == "") {
                failureMsg("请先选择日期！")
                return false;
            }

            openwindow("/kcManager/commitPage?startTime=" + startTime + "&endTime=" + endTime+ "&upCode=" + upCode, "选择审批流程", "800", "320", false, reload);
            /*$.get("",data,success){
                
            }*/

        },
        saveKclist: function () {
            if (vm.kcInfo.length == 0) {
                failureMsg("未发现需要保存的数据！");
                return false;
            }
            var data = {
                kcInfo: JSON.stringify(vm.kcInfo),
                startTime: $("#startTime").val(),
                endTime: $("#endTime").val()
            }
            $.post("/kcManager/saveKcList", data, function (resp) {
                vm.serialNumber = resp;
            })
        },
        refreshList: function (calcData, kcList, type) {
            var baseData = vm.kcInfo.data;
            var currData = baseData[kcList.groupId];
            var baseFee = calcData[2]; //基础
            var ksCount = calcData[3]; //实际
            var personFactor = calcData[4]; //人数
            var courseFactor = calcData[5]; //课程
            var data1 = Decimal.mul(baseFee, ksCount);
            var data2 = Decimal.mul(personFactor, courseFactor);
            var kcCount = Decimal.mul(data1, data2);
            var feeType = calcData[6];
            var projectCode = calcData[7]; //projectId
            kcCount = vm.format(kcCount.valueOf());
            var data = {
                timeSolt: kcList.timeSolt,
                courseName: "",
                language: "",
                teacherName: kcList.teacherName,
                teacherCode: kcList.teacherCode,
                teacherType: kcList.teacherType,
                projectName: calcData[1],
                projectId: calcData[0],
                baseFee: baseFee.valueOf(),
                ksCount: ksCount.valueOf(),
                personFactor: personFactor.valueOf(),
                courseFactor: courseFactor.valueOf(),
                kcCount: kcCount,
                projectCode: projectCode.valueOf(),
                px: kcList.px,
                groupId: kcList.groupId,
                feeType: feeType
            }
            if (type == "add") {
                var dataLast = currData.pop();
                currData.push(data);
                var xjksCount = "0";
                var xjkcCount = "0.00";
                for (var i = 0; i < currData.length; i++) {
                    var curr = currData[i];
                    if (curr.px != '小计') {
                        if (curr.projectId == '1901') {
                            xjkcCount = Decimal.sub(xjkcCount, curr.kcCount);
                            xjksCount = Decimal.sub(xjksCount, curr.ksCount);
                        } else {
                            xjkcCount = Decimal.add(xjkcCount, curr.kcCount);
                            xjksCount = Decimal.add(xjksCount, curr.ksCount);
                        }
                    }
                }
                dataLast.ksCount = xjksCount.valueOf();
                dataLast.kcCount = vm.format(xjkcCount.valueOf());
                currData.push(dataLast);

            } else {
                var dataLast = currData.pop();
                var index = 0;
                for (var i = 0; i < currData.length; i++) {
                    if (currData[i].px == data.px) {
                        index = i;
                        break;
                    }
                }
                currData[index] = data;
                var xjksCount = "0";
                var xjkcCount = "0.00";
                for (var i = 0; i < currData.length; i++) {
                    if (currData[i].px != '小计') {
                        if (currData[i].projectId == '1901') {
                            xjkcCount = Decimal.sub(xjkcCount, currData[i].kcCount);
                            xjksCount = Decimal.sub(xjksCount, currData[i].ksCount);
                        } else {
                            xjkcCount = Decimal.add(xjkcCount, currData[i].kcCount);
                            xjksCount = Decimal.add(xjksCount, currData[i].ksCount);
                        }
                    }
                }
                dataLast.ksCount = xjksCount.valueOf();
                dataLast.kcCount = vm.format(xjkcCount.valueOf());
                currData.push(dataLast);
            }
            //重拍序号
            var num = 1;
            var hjKcCount = "0.00";
            var hjKsCount = "0";
            for (var i = 0; i < baseData.length; i++) {
                var groupData = baseData[i];
                for (var j = 0; j < groupData.length; j++) {
                    if (groupData[j].px != '小计' && groupData[j].px != '合计') {
                        groupData[j].px = num;
                        num++;
                    } else if (groupData[j].px != '合计') {
                        hjKcCount = Decimal.add(hjKcCount, groupData[j].kcCount);
                        hjKsCount = Decimal.add(hjKsCount, groupData[j].ksCount);
                    }
                }
            }
            var hjData = baseData[baseData.length - 1];
            hjData[0].kcCount = kcCount = vm.format(hjKcCount.valueOf());
            hjData[0].ksCount = ksCount = hjKsCount.valueOf();
            //保存
            this.saveKclist();
        },
        format: function (str) {
            /*str = str.toString();
            if(str.indexOf(".")>=0){
                var strArr = str.split(".")
                if(strArr[1].length==2){
                    return str;
                }else{
                    return str+"0";
                }
            }else{
                return str+".00";
            }*/
            return Number(str).toFixed(2);
        },
        search: function (selectParam) {
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            // var nowTime = getNowFormatDate();
            //var teacherName = $("#teacherName").val();
            var flag = false;
            if (startTime == "" || endTime == "") {
                failureMsg("开始日期和结束日期不能为空！");
                return false;
            }
            if (startTime > endTime) {
                failureMsg("开始日期不能大于结束日期！");
                return false;
            }
            var data = {
                startTime: startTime,
                endTime: endTime,
                //teacherName:teacherName,
                selectParam: selectParam
            }
           if (vm.flag != '1') {
                $.get("/kcManager/checkSave", data, function (resp) {
                    if (resp.result == 'OK') {
                        vm.queryKc(data);
                    } else {
                        failureMsg(resp.message);
                        return false;
                    }
                })
            } else {
                vm.queryKc(data);
            }
        },
        queryKc: function (data) {
            //如果存在历史，就直接将时间段统计出的数据抓出来展示 //如果不存在历史，就重新统计数据
            $.get("/kcManager/queryKcInfo", data, function (resp) {
                console.info(resp);
                vm.kcInfo = resp;
                var count = 0;
                var result = [];
                for (var i = 0; i < resp.data.length; i++) {
                    var data = resp.data[i];
                    if (i < vm.limit) {
                        result.push(data);
                    }
                    for (var j = 0; j < data.length; j++) {
                        if (data[j].px != '小计' && data[j].px != '合计') {
                            count++;
                        }
                    }
                }
                vm.count = count;
            })
        },
        functionClassify: function (kcList) {
        	//alert(kcList.projectId.length);
           if (kcList.groupId == undefined) {
                return false;
            }
            if (kcList.projectId.length > 5) {
                this.addHang(kcList);
            }
            if (kcList.projectId.length < 5) {
                this.modifyHang(kcList);
            }
        },
        kcDetail:function(time,teacherCode,courseId,projectId) {
        	if(courseId == undefined){
        		return false;
        	}
        	openwindow("/kcManager/showKCDetail?time=" + time + "&teacherCode=" + teacherCode + "&courseId=" + courseId + "&projectId=" + projectId, "课酬明细", "1300", "800", false, null);
        },
        modifyHang: function (kcList) {
            var data = {
                timeSolt: kcList.timeSolt,
                projectCode: kcList.projectCode,
                teacherName: kcList.teacherName,
                teacherCode: kcList.teacherCode,
                teacherType: kcList.teacherType,
                projectId: kcList.projectId,
                projectName: kcList.projectName,
                baseFee: kcList.baseFee,
                ksCount: kcList.ksCount,
                personFactor: kcList.personFactor,
                courseFactor: kcList.courseFactor,
                kcCount: kcList.kcCount,
                feeType: kcList.feeType,
                scheduleType: kcList.scheduleType,
                groupId: kcList.groupId,
                px: kcList.px
            }
            //查询其他项目名称 工作量扣除，讲座
            var selectHtml = '';
            var html = '';
            $.get("/kcManager/queryOtherProject", function (resp) {
                selectHtml += '<form class="layui-form" action=""><select lay-filter="test1" class="selProject">';
                $.each(resp, function (i, item) {
                    if (data.projectId == item.code) {
                        selectHtml += '<option value="' + item.code + '"  selected>' + item.name + '</option>';
                    } else {
                        selectHtml += '<option value="' + item.code + '" >' + item.name + '</option>';
                    }
                })
                selectHtml += '</select></form>';
                html += '<div>';
                html += '<table class="layui-table" lay-skin="line">';
                html += '<thead class="thCenter"><tr>';
                html += '<th>序号</th><th width="13%">时段</th><th width="13%">项目名称</th><th>教师姓名</th><th width="14%">教师类别</th>';
                html += '<th>基础课酬</th><th>实际课时</th><th>人数系数</th><th>课程系数</th><th>课酬（元）</th><th width="10%">备注</th>';
                html += '</thead>';
                html += '<tbody>';
                html += '<tr>';
                html += '<td style="width:5%;">1</td>';
                html += '<td style="width:15%;">' + data.timeSolt + '</td>';
                html += '<td>';
                html += selectHtml;
                html += '</td>';
                html += '<td class="inp">' + data.teacherName + '</td>';
                html += '<td class="inp">' + data.teacherType + '</td>';
                html += '<td class="inp"><input id="baseFee" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.baseFee + '"></td>';
                html += '<td class="inp"><input id="ksCount" autocomplete="off" onblur="isPosNum(this)" type="text" value="' + data.ksCount + '"></td>';
                html += '<td class="inp"><input id="personFactor" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.personFactor + '"></td>';
                html += '<td class="inp"><input id="courseFactor" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.courseFactor + '"></td>';
                html += '<td id="kcCount">' + data.kcCount + '</td>';
                html += '<td class="inp"><input type="text" id="feeType" value="' + data.feeType + '"></td>';
                html += '<td class="inp" style="display: none"><input id="projectCode" type="hidden" value="' + data.projectCode + '"></td>';
                html += '</tr>';
                html += '</tbody>';
                html += '</table>';
                html += '</div>'

                layer.open({
                    type: 1,
                    title: "新增",
                    btn: ["保存", "返回"],
                    closeBtn: 1,
                    area: ['1280px', '50%'],
                    content: html,
                    success: function () {
                        form.render('select');
                    },
                    yes: function (index, layero) {
                        var table = layero.find('table')[0];
                        var pName = "";
                        var flag = true;
                        var calcData = [];
                        var tdData = $($($(table).find('tbody')[0]).find('tr')[0]).find('td');
                        
                        tdData.find("form").find("dd").each(function () {
                            var val = $(this).attr("class");
                            if (val == "layui-this") {
                                pValue = $(this).attr("lay-value");
                                pName = $(this).html();
                                calcData.push(pValue);
                                // calcData.push(pName);
                            }
                        });
                        tdData.find("input").each(function () {
                            calcData.push($(this).val());

                        })
                        vm.refreshList(calcData, data, "modify");
                        layer.close(index);
                        /*$.each(tdData, function (i, item) {
                            if (calcData.length == 0) {
                                $.each($(item).find('select option:selected'), function (i, item) {
                                    pValue = item.value;
                                    pName = item.innerText;
                                    calcData.push(pValue);
                                    calcData.push(pName);
                                })
                            } else {
                                return false;
                            }
                        })
                        var num = 0;
                        $.each(tdData, function (i, item) {
                            $.each($(item).find('input'), function (j, item) {
                                if (num == 1) {
                                    if (item.value == '') {
                                        flag = false;
                                        return false;
                                    }
                                    var exp = /^[1-9]*[0-9][0-9]*$/;
                                    if (!exp.test(item.value)) {
                                        var value = vm.returnFloat(item.value);
                                        item.value = value;
                                        /!*flag = false;
                                        return false;*!/
                                    }
                                } else if (num == 4) {

                                } else if (num == 5) {

                                }
                                else {
                                    if (item.value == '') {
                                        flag = false;
                                        return false;
                                    }
                                    var exp = /^(([1-9]\d*)|\d)\.(\d{2})?$/;
                                    if (!exp.test(item.value)) {
                                        var value = vm.returnFloat(item.value);
                                        item.value = value;
                                        /!*flag = false;
                                        return false;*!/
                                    }
                                }

                                /!*if(num==1){
                                    var exp = /^[1-9]*[0-9][0-9]*$/;
                                    if(!exp.test(item.value)){
                                        flag = false;
                                        return false;
                                    }
                                }else if(num==4){
                                    
                                }else{
                                    var exp = /^(([1-9]\d*)|\d)\.(\d{2})?$/;
                                    if(!exp.test(item.value)){
                                        flag = false;
                                        return false;
                                    }
                                }*!/
                                calcData.push(item.value);
                                num++;
                            })
                        })
                        if (calcData.length == 1 || !flag) {
                            failureMsg('参数格式校验错误！');
                            return false;
                        } else {
                            vm.refreshList(calcData, data, "modify");
                            layer.close(index);
                        }*/
                    }
                })
            })
        },
        addHang: function (kcList) {   //添加行
            //时段、教室姓名、教室类别
            var data = {
                timeSolt: kcList.timeSolt,
                teacherName: kcList.teacherName,
                teacherCode: kcList.teacherCode,
                teacherType: kcList.teacherType,
                projectId: "",
                projectName: "",
                baseFee: "",
                ksCount: "",
                personFactor: "",
                courseFactor: "",
                kcCount: "",
                feeType: "0",
                groupId: kcList.groupId,
                projectCode: kcList.projectId
            }
            var selectHtml = '';
            var html = '';
            //查询当前教师上次课酬计算是否为工作量扣除
            $.get("/kcManager/queryLastProject", data, function (resp) {
                /*if (resp != "") {
                    data.projectId = resp.projectId;
                    data.projectName = resp.projectName;
                    data.baseFee = resp.baseFee;
                    data.ksCount = resp.hourActual;
                    data.personFactor = resp.personFactor;
                    data.courseFactor = resp.courseFactor;
                    data.kcCount = resp.feeCourse;
                    data.serialNumber = resp.serialNumber;
                }
*/
                //查询其他项目名称 工作量扣除，讲座
                var selectHtml = '';
                var html = '';
                $.get("/kcManager/queryOtherProject", function (resp) {
                    selectHtml += '<form class="layui-form" action=""><select lay-filter="test" class="selProject">';
                    $.each(resp, function (i, item) {
                        if (data.projectId == item.code) {
                            selectHtml += '<option value="' + item.code + '"  selected>' + item.name + '</option>';
                        } else {
                            selectHtml += '<option value="' + item.code + '" >' + item.name + '</option>';
                        }
                    })
                    selectHtml += '</select></form>';
                    html += '<div>';
                    html += '<table class="layui-table" lay-skin="line">';
                    html += '<thead class="thCenter" ><tr>';
                    html += '<th>序号</th><th width="18%">时段</th><th width="15%">项目名称</th><th>教师姓名</th><th width="15%">教师类别</th>';
                    html += '<th>基础课酬</th><th>实际课时</th><th>人数系数</th><th>课程系数</th><th>课酬（元）</th><th>备注</th>';
                    html += '</thead>';
                    html += '<tbody>';
                    html += '<tr>';
                    html += '<td>1</td>';
                    html += '<td>' + data.timeSolt + '</td>';
                    html += '<td>';
                    html += selectHtml;
                    html += '</td>';
                    html += '<td class="inp">' + data.teacherName + '</td>';
                    html += '<td class="inp">' + data.teacherType + '</td>';
                    html += '<td class="inp"><input id="baseFee" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.baseFee + '"></td>';
                    html += '<td class="inp"><input id="ksCount" autocomplete="off" onblur="isPosNum(this)" type="text" value="' + data.ksCount + '"></td>';
                    html += '<td class="inp"><input id="personFactor" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.personFactor + '"></td>';
                    html += '<td class="inp"><input id="courseFactor" autocomplete="off" onblur="isMoneyNum(this)" type="text" value="' + data.courseFactor + '"></td>';
                    html += '<td id="kcCount" style="width:8%;">' + data.kcCount + '</td>';
                    html += '<td class="inp"><input id="feeType" autocomplete="off" type="text" value=""></td>';
                    html += '<td style="display:none">' + data.groupId + '</td>';
                    html += '<td class="inp" style="display:none"><input id="projectCode" type="hidden" value="' + data.projectCode + '"></td>';
                    html += '</tr>';
                    html += '</tbody>';
                    html += '</table>';
                    html += '</div>';
                    layer.open({
                        type: 1,
                        title: "新增",
                        btn: ["保存", "取消"],
                        closeBtn: 1,
                        area: ['1300px', '50%'],
                        content: html,
                        success: function () {
                            form.render('select');
                        },
                        yes: function (index, layero) {
                            var table = layero.find('table')[0];
                            var pName = "";
                            var flag = true;
                            var calcData = [];
                            var tdData = $($($(table).find('tbody')[0]).find('tr')[0]).find('td');
                            tdData.find("form").find("dd").each(function () {
                                var val = $(this).attr("class");
                                if (val == "layui-this") {
                                    pValue = $(this).attr("lay-value");
                                    pName = $(this).html();
                                    calcData.push(pValue);
                                    // calcData.push(pName);
                                }
                            });
                            tdData.find("input").each(function () {
                                calcData.push($(this).val());

                            })
                            vm.refreshList(calcData, data, "add");
                            layer.close(index);
                            /*$.each(tdData,function(i,item){
                                if(calcData.length==0){
                                    $.each($(item).find('select option:selected'),function(i,item){
                                        pValue = item.value;
                                        pName = item.innerText;
                                        calcData.push(pValue);
                                        calcData.push(pName);
                                    })
                                }else{
                                    return false;
                                }
                            })*/
                            /*var num=0;
                            $.each(tdData,function(i,item){
                                $.each($(item).find('input'),function(j,item){
                                    if(num==1){
                                        if(item.value == ''){
                                            flag = false;
                                            return false;
                                        }
                                        var exp = /^[1-9]*[0-9][0-9]*$/;
                                        if(!exp.test(item.value)){
                                            var value = vm.returnFloat(item.value);
                                            item.value = value;
                                            /!*flag = false;
                                            return false;*!/
                                        }
                                    }else if(num==4){
                                        
                                    }else if(num == 5){
                                        
                                    }else{
                                        if(item.value == ''){
                                            flag = false;
                                            return false;
                                        }
                                        var exp = /^(([1-9]\d*)|\d)\.(\d{2})?$/;
                                        if(!exp.test(item.value)){
                                            var value = vm.returnFloat(item.value);
                                            item.value = value;
                                            /!*flag = false;
                                            return false;*!/
                                        }
                                    }
                                    calcData.push(item.value);
                                    num++;
                                })
                            })
                            if(calcData.length==1 || !flag){
                                failureMsg('参数不能为空！');
                                return false;
                            }else{
                                vm.refreshList(calcData,data,"add");
                                layer.close(index);
                            }*/

                        }
                    })
                })
            })
        },
        returnFloat: function (value) {
            var value = Math.round(parseFloat(value) * 100) / 100;
            var xsd = value.toString().split(".");
            if (xsd.length == 1) {
                value = value.toString() + ".00";
                return value;
            }
            if (xsd.length > 1) {
                if (xsd[1].length < 2) {
                    value = value.toString() + "0";
                }
                return value;
            }
        }

    }
});

function isPosNum(obj) {
    var number = $(obj).val();
    number = Number(number);
    if (isNaN(number)) {
        $(obj).val(0);
    }
    $(obj).val(number.toFixed(0));
    var reg = /^(?:0|\-?(?:0\.\d*[1-9]|[1-9]\d*(?:\.\d*[1-9])?))$/;
    if (!reg.test(number)) {
        $(obj).val(0);
    }
}

function delValue(){
	console.log(111111)
}

function isMoneyNum(obj) {
    var number = $(obj).val();
    number = Number(number);
    if (isNaN(number)) {
        $(obj).val(0);
    }
    $(obj).val(Number(number.toFixed(2)).toFixed(2));x
    var reg = /^(?:0|\-?(?:0\.\d*[1-9]|[1-9]\d*(?:\.\d*[1-9])?))$/;
    if (!reg.test(number)) {
        $(obj).val(0);
    }
}

function reload() {
    vm.serialNumber = "";
    /*window.location.reload();*/
    window.location.href = "queryKcPage";
}

//下拉框效果
$(document).on('click', function () {
    $('.pxkeji-select').find('.layui-anim-upbit').hide();
    $('.pxkeji-select').find('.layui-edge').css({'transform': 'rotate(0deg)', 'margin-top': '-3px'});
})
$(document).on("click", ".layui-select-title", function (e) {
    e.stopPropagation();
    if ($(this).parents('.pxkeji-select').find('.layui-anim-upbit').css('display') == "block") {
        $(this).parents('.pxkeji-select').find('.layui-anim-upbit').hide();
        $(this).parents('.pxkeji-select').find('.layui-edge').css({'transform': 'rotate(0deg)', 'margin-top': '-3px'});
    } else {
        $(this).parents('.pxkeji-select').find('.layui-anim-upbit').show();
        $(this).parents('.pxkeji-select').find('.layui-edge').css({
            'transform': 'rotate(180deg)',
            'margin-top': '-9px'
        });
    }
})
$(document).on("click", ".pxkeji-select dd", function () {
    $(this).parents('.pxkeji-select').find('.layui-edge').css({'transform': 'rotate(0deg)', 'margin-top': '-3px'});
    $(this).parent().find('dd').removeClass('layui-this')
    $(this).addClass('layui-this');
    $(this).parents('.pxkeji-select').find('input').val($(this).html());
    $(this).parents('.pxkeji-select').find('input').attr('data-value', $(this).attr('lay-value'))
    $(this).parents('.pxkeji-select').find('.layui-anim-upbit').hide()
});

window.onbeforeunload = function () {
    if (checkCommit()) return ('确定离开当前页面吗?未提交的数据可能会丢失!');
}

function checkCommit() {
    if (vm.serialNumber != '') {
        return true;
    } else {
        return false;
    }
}

//获取当前时间，格式YYYY-MM-DD
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
    

/*添加学生页面*/
var formSelects;
$("#add_saveStudent").click(function () {
    //非空验证姓名
    var name = $("input[name='name']").val();
    if (name == null || name == undefined || name == "") {
        tipinfo("学生姓名不能为空");
        return;
    }
    //非空验证英文姓名
    /*var englishName = $("input[name='englishName']").val();
    if(englishName == null || englishName == undefined || englishName == ""){
        tipinfo("英文名称不能为空");
        return ;
    }*/
    //非空验证学号
    var serial = $("input[name='serial']").val();
    if (serial == null || serial == undefined || serial == "") {
        tipinfo("学生学号不能为空");
        return;
    }
    //身份证号验证
    var reg = /(\d{15}$)|(\d{17}(?:\d|x|X))/;
    var idcard = $("input[name='idcard']").val();
    if (idcard != null && idcard != "" && reg.test(idcard) == false) {
        tipinfo("身份证输入不合法");
        return false;
    }
    //验证手机号
    var isPhone = /^1[0-9]{10}$/;//手机号码
    var telephone = $("input[name='telephone']").val();
    if (telephone != null && telephone != "" && isPhone.test(telephone) === false) {
        tipinfo("手机号输入不合法");
        return false;
    }
    var roleList = formSelects.value('selects2', 'val');
    // if (roleList.length < 1) {
    //     tipinfo("必须选择项目");
    //     return false;
    // }

    var student = $("#student").serialize();
    $.ajax({
        type: "POST",
        url: "/student/saveStudent",
        data: student,
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
$("#add_editStudent").click(function () {
    //非空验证姓名
    var name = $("input[name='name']").val();
    if (name == null || name == undefined || name == "") {
        tipinfo("学生姓名不能为空");
        return;
    }
    //非空验证英文姓名
    /*var englishName = $("input[name='englishName']").val();
    if(englishName == null || englishName == undefined || englishName == ""){
        tipinfo("英文名称不能为空");
        return ;
    }*/
    //非空验证学号
    var serial = $("input[name='serial']").val();
    if (serial == null || serial == undefined || serial == "") {
        tipinfo("学生学号不能为空");
        return;
    }
    //身份证号验证
    var reg = /(\d{15}$)|(\d{17}(?:\d|x|X))/;
    var idcard = $("input[name='idcard']").val();
    if (idcard != null && idcard != "" && reg.test(idcard) == false) {
        tipinfo("身份证输入不合法");
        return false;
    }
    //验证手机号
    var isPhone = /^1[0-9]{10}$/;//手机号码
    var telephone = $("input[name='telephone']").val();
    if (telephone != null && telephone != "" && isPhone.test(telephone) === false) {
        tipinfo("手机号输入不合法");
        return false;
    }
    var roleList = formSelects.value('selects2', 'val');
    // if (roleList.length < 1) {
    //     tipinfo("必须选择项目");
    //     return false;
    // }
    var student = $("#student").serialize();
    $.ajax({
        type: "POST",
        url: "/student/updateStudent",
        data: student,
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
$("#add_closeWindow").click(function () {
    closewindow();
});
layui.use(["form", "upload", "formSelects"], function () {
    var form = layui.form;
    formSelects = layui.formSelects;
    var upload = layui.upload;
    $(function () {
        form.render();
    });


    //绑定原始文件域
    upload.render({
        elem: "#studentPic"
        , url: "/student/queryUploadStudent"
        , done: function (data) {
            if (data.state) {
                $("input[name='pic']").val(data.data.url);
                $("#upPicName").html(data.data.name);
                $("#imgPic").remove();
                tipinfo("上传成功");
            } else {
                tipinfo("上传失败");
            }
        }
    });
    formSelects.filter('example4', function (id, inputVal, val, isDisabled) {
        if (
            PY.fullPY(val.name).toLowerCase().indexOf(inputVal) != -1 ||    //拼音全拼是否包含
            PY.fullPY(val.name, true).indexOf(inputVal) != -1 ||            //拼音简拼是否包含
            val.name.indexOf(inputVal) != -1                                //文本是否包含
        ) {
            return false;
        }
        return true;
    });
});
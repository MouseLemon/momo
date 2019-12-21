/**
 * 课酬管理-课酬统计
 * */
var laypage;
var formSelects;
layui.use(["form","formSelects"], function () {
    var form = layui.form;
    formSelects = layui.formSelects;
   
    $(function () {
        form.render();
    });

    //获取未完结的项目
    function projects() {
        $.ajax({
            type: "GET",
            url: "/student/",
            dataType: "JSON",
            success: function (data) {
                //添加到项目的下拉框中
            }
        })
    }
 
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
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		productName:[],
		teacherCodeList:[],
		personCode:"",
		personList:[]
	},
	mounted:function(){
		this.search();
		$.get("/api/organizePeople/getAllPersonUser",function(data){
			vm.personList = data;
		},"json");
        setTimeout(function () {
            var dataHeight=$('.layui-thead').height()+$('.mytbody').height();
            var documentHeight=$(window).height()-280;
            if(dataHeight<documentHeight){
                $('.table_box_big').css('height',dataHeight+20);
                $('.table_tbody_box').css('height',dataHeight);
            }else{
                $('.table_box_big').css('height',documentHeight+20);
                $('.table_tbody_box').css('height',documentHeight);
            }
        },200);
	},
	methods:{
		KCInfo:function(serialNumber,teacherCode,mergeSerialNumber,projectIds){
			window.location.href = "/kcManager/kcDetailed?serialNumber="+serialNumber+"&teacherCode="+teacherCode
				+"&mergeSerialNumber="+mergeSerialNumber+"&projectIds="+projectIds;
		},
		userInfo:function(code){
			layer.open({
				type: 2,
				content: '/unSend/teacherInfo?teacherCode='+code,
				title: '教师基本信息表',
				resize:false,
				area: ['70%', '24%']
			})
		},
		search:function(){
			var limit=10;
			var currentPage = 1;
			var data={
				limit : limit,
				currentPage :currentPage,
				status:'2',
				time:$("#time").val(),
				teacherName:$.trim($("#teacherName").val()),
				note:$("#note").val()
			}
			$.get("/unSend/getProductName?status=2",function(resp){
				vm.productName = resp;
			});
            $.get("/unSend/getUnsendData", data, function (resp) {
                vm.kcInfo = resp;
                console.log(vm.kcInfo)
            });
		},
		ensureSend:function(){
			var teacherCode = [];
            $("input[name='teacherKcSend']").each(function(){
                if(this.checked == true) {
                    teacherCode.push($(this).val());
                }
            });
			var data=[];
			 for(var i=0;i<teacherCode.length;i++){
                 // var approveOption = $(layero).find('form')[0].find('textarea').val();
                 var teacherCodeOne = teacherCode[i].split("_");
                 var dataArr={};
                 for(var j=0;j<teacherCodeOne.length;j++){
                     dataArr={
                         mergeSerialNumber:teacherCodeOne[0],
                         serialNumber:teacherCodeOne[1],
                         teacherCode:teacherCodeOne[2],
                         projectIds:teacherCodeOne[3],
                         year:teacherCodeOne[4],
                         month:teacherCodeOne[5],
                         totalCount:teacherCodeOne[6],
                         depType:'3',
                         approveOrder:'',
                         personCode:vm.personCode,
                         status:'1'
                         // approveOption:approveOption
                     }
                 }
                 data.push(dataArr);
             }
			if(data.length == 0){
				failureMsg("请选择数据");
				return false;
			}
	    	layer.open({
				type: 1,
				title: "警告：",
				btn:["确定","取消"],
				btnAlign: 'c',
				closeBtn: 1,
				area:['30%','40%'],
				content: $("#shenHe").html(),
				btn1: function(index, layero){
					var msgList = formSelects.value('selects2', 'val');
                    $.post("/approve/cwkcApproval",{data:JSON.stringify(data),kcStatus:"3",pass:"1",personIds:JSON.stringify(msgList)},function(resp){
                        if(resp.result==='OK'){
                            successMsg("发放成功！");
                            vm.search();
                            $("input[type=checkbox]").prop("checked",false);
                            layer.close(index);
                        }else{
                            // failureMsg("审核失败："+resp.message);
                            failureMsg("发放失败");
                        }
                    })
				}
			})
		},
		returnExamine:function(){
			var teacherCode = [];
            $("input[name='teacherKcSend']").each(function(){
                if(this.checked == true) {
                    teacherCode.push($(this).val());
                }
            });
			var data=[];
			for(var i=0;i<teacherCode.length;i++){
				// var approveOption = $(layero.find('form')[0]).find('textarea').val();
				var teacherCodeOne = teacherCode[i].split("_");
				var dataArr={};
				for(var j=0;j<teacherCodeOne.length;j++){
					dataArr={
						mergeSerialNumber:teacherCodeOne[0],
						serialNumber:teacherCodeOne[1],
						teacherCode:teacherCodeOne[2],
                        projectIds:teacherCodeOne[3],
						depType:'3',
						approveOrder:'',
						personCode:vm.personCode,
						status:'0',
						// approveOption:approveOption
					}
				}
				data.push(dataArr);
			}
			if(data.length == 0){
				failureMsg("请选择数据!");
				return false;
			}
	    	layer.open({
				type: 1,
				title: "提示：",
				btn:["确定","取消"],
				btnAlign: 'c',
				closeBtn: 1,
				area:['25%','30%'],
				content: $("#approvePage").html(),
				btn1: function(index, layero){
                    var approveOption = $(layero.find('form')[0]).find('textarea').val();
					$.post("/approve/cwkcApproval",{data:JSON.stringify(data),kcStatus:"-3",pass:"-1",approveOption:approveOption},function(resp){
						if(resp.result==='OK'){
                            tipinfo("退回成功！");
							vm.search();
							layer.close(index);
						}else{
                            tipinfo("退回失败！"+resp.message);
						}
					})
				}
			})
		},
		exportExcel:function(){
			var teacherName = $.trim($("#teacherName").val());
			var time = $.trim($("#time").val());
			var note = $.trim($("#note").val());
			window.location.href="/unSend/exportUnSendKc?teacherName=" + teacherName + "&time=" + time + "&note=" + note;
		}
	}
})

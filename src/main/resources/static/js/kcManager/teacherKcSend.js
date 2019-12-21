/**
 * 课酬管理-课酬统计
 * */
var laypage;
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		productName:[],
		teacherCodeList:[],
		personCode:""
	},
	created:function(){
		this.search();
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
				area: ['70%', '30%']
			})
		},
		exportExcel:function(){
			var teacherName = $("#teacherName").val();
			var time = $("#time").val();
            var note = $.trim($("#note").val());
			window.location.href="/unSend/exportSendTeacherKC?teacherName="+teacherName+"&time="+time+"&note=" + note;
		},
		search:function(){
			var limit=10;
			var currentPage = 1;
			var data={
				limit : limit,
				currentPage :currentPage,
				status:'3',
				time:$("#time").val(),
				teacherName:$.trim($("#teacherName").val()),
                note:$.trim($("#note").val())
			}
			$.get("/unSend/getProductName?status=3",function(resp){
				vm.productName = resp;
			})
			$.get("/unSend/getUnsendData",data,function(resp){
				vm.kcInfo = resp;
				
			})
		},
		ensureSend:function(){
			var teacherCode = vm.teacherCodeList;
			var data=[];
	    	layer.open({
				type: 1,
				title: "警告：",
				btn:["确定","取消"],
				btnAlign: 'c',
				closeBtn: 1,
				area:['30%','35%'],
				content: $("#shenHe").html(),
				btn1: function(index, layero){
					for(var i=0;i<teacherCode.length;i++){
						var approveOption = $(layero.find('form')[0]).find('textarea').val();
						var teacherCodeOne = teacherCode[i].split("_");
						var dataArr={};
						for(var j=0;j<teacherCodeOne.length;j++){
							dataArr={
								mergeSerialNumber:teacherCodeOne[0],
								serialNumber:teacherCodeOne[1],
								teacherCode:teacherCodeOne[2],
								depType:'3',
								approveOrder:'',
								personCode:vm.personCode,
								status:'1',
								approveOption:approveOption
							}
						}
						data.push(dataArr);
					}
					$.get("/approve/cwkcApproval",{data:JSON.stringify(data),kcStatus:"3",pass:"1"},function(resp){
						if(resp.result==='OK'){
							successMsg("审核成功！");
							vm.search();
							layer.close(index);
						}else{
							failureMsg("审核失败："+resp.message);
						}
					})
				}
			})
		},
		returnExamine:function(){
			var teacherCode = vm.teacherCodeList;
			var data=[];
	    	layer.open({
				type: 1,
				title: "提示：",
				btn:["确定","取消"],
				btnAlign: 'c',
				closeBtn: 1,
				area:['25%','30%'],
				content: $("#approvePage").html(),
				btn1: function(index, layero){
					for(var i=0;i<teacherCode.length;i++){
						var approveOption = $(layero.find('form')[0]).find('textarea').val();
						var teacherCodeOne = teacherCode[i].split("_");
						var dataArr={};
						for(var j=0;j<teacherCodeOne.length;j++){
							dataArr={
								mergeSerialNumber:teacherCodeOne[0],
								serialNumber:teacherCodeOne[1],
								teacherCode:teacherCodeOne[2],
								depType:'3',
								approveOrder:'',
								personCode:vm.personCode,
								status:'0',
								approveOption:approveOption
							}
						}
						data.push(dataArr);
					}
					$.get("/approve/cwkcApproval",{data:JSON.stringify(data),kcStatus:"-3",pass:"-1"},function(resp){
						if(resp.result==='OK'){
							successMsg("审核成功！");
							vm.search();
							layer.close(index);
						}else{
							failureMsg("审核失败："+resp.message);
						}
					})
				}
			})
		}
		
	}
})

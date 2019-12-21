	/**
 * 课酬管理-已发教师课酬
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
	},
	methods:{
		userInfo:function(){
			layer.open({
				type: 2,
				content: '/kcManagerTeacher/teacherInfo',
				title: '教师基本信息表',
				resize:false,
				area: ['70%', '27%']
			})
		},
		search:function(){
			var limit=10;
			var currentPage = 1;
			var data={
				limit : limit,
				currentPage :currentPage,
				time:$("#time").val(),
				teacherName:$.trim($("#teacherName").val())
			}
			$.get("/unSend/getProductName",function(resp){
				vm.productName = resp;
			})
			$.get("/unSend/getUnsendData",data,function(resp){
				vm.kcInfo = resp
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
				area:['30%','30%'],
				content: $("#shenHe").html(),
				btn1: function(index, layero){
					for(var i=0;i<teacherCode.length;i++){
						var approveOption = $(layero.find('form')[0]).find('textarea').val();
						var teacherCodeOne = teacherCode[i].split("_");
						var dataArr={};
						for(var j=0;j<teacherCodeOne.length;j++){
							dataArr={
								serialNumber:teacherCodeOne[0],
								teacherCode:teacherCodeOne[1],
								depType:teacherCodeOne[2],
								approveOrder:teacherCodeOne[3],
								personCode:vm.personCode,
								status:'1',
								approveOption:approveOption
							}
						}
						data.push(dataArr);
					}
					$.get("/approve/kcPass",{data:JSON.stringify(data),kcStatus:"3",pass:"1"},function(resp){
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
								serialNumber:teacherCodeOne[0],
								teacherCode:teacherCodeOne[1],
								depType:teacherCodeOne[2],
								approveOrder:teacherCodeOne[3],
								personCode:vm.personCode,
								status:'0',
								approveOption:approveOption
							}
						}
						data.push(dataArr);
					}
					$.get("/approve/kcPass",{data:JSON.stringify(data),kcStatus:"-3",pass:"-1"},function(resp){
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
		/*,
		exportExcel:function(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime=="" || endTime==""){
				failureMsg("搜索条件不能为空!");
				return false;
			}
			window.location.href="/kcManagerTeacher/exportTeacherKC?startTime="+startTime+"&endTime="+endTime;
		}*/
	}
})
/*var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		productName:[]
	},
	created:function(){
		$.get("/teacherKc/getkcTeacher",function(resp){
			vm.kcInfo = resp.data;
		})
		$.get("/teacherKc/getProductName",function(resp){
			vm.productName = resp.data;
			console.log("項目"+resp.data);
		})
	},
	methods:{
		userInfo:function(){
			layer.open({
				type: 2,
				skin: 'layui-layer-molv',
				content: '/kcManagerTeacher/teacherInfo',
				title: '教师基本信息表',
				resize:false,
				area: ['65%', '40%']
			})
		},
		detail:function(){
			layer.open({
				type: 2,
//				skin: 'layui-layer-molv',
				content: '/kcManagerTeacher/kcInfo',
				title: '教师课酬明细',
				resize:false,
				area: ['70%', '40%']
			})
		},
		search:function(){
			//alert(1)
			var yearAndMonth1 = $("#yearAndMonth1").val();
			var teacherType = $("#teacherType").val();
			if(yearAndMonth1 =="" || yearAndMonth1==null){
				failureMsg("请选择时间段！");
				return false;
			}
			var data={
				yearAndMonth:yearAndMonth1,
				teacherType:teacherType
			}
			$.get("/teacherKc/getProductName",function(resp){
				vm.productName = resp.data;
				//console.log("項目"+resp.data);
			})
			$.get("/teacherKc/getkcTeacher",data,function(resp){
				vm.kcInfo = resp.data;
				laypage.render({
					elem: 'jqPager', //元素id
					layout: ['prev', 'page', 'next', 'skip', 'count'],
					limit:limit,
					count:resp.count,
					curr: currentPage,
					jump: function(obj, first){
						currentPage = obj.curr;
						limit = obj.limit;
						if(!first){
							//queryTiLibrary(pointCodes);
						}
					}
				});
		})
	}
	}
})*/
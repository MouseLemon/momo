/**
 * 课酬管理-课酬统计
 * */
var laypage;
layui.use(['laydate','laypage'],function(){
	var laydate = layui.laydate;
	laypage = layui.laypage;

	laydate.render({
    	elem: '#startTime'
    	,type: 'year'
 	});
	laydate.render({
    	elem: '#endTime'
    	,type: 'year'
 	});
})
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		productName:[]
	},
	created:function(){
		
	},
	methods:{
		userInfo:function(){
			layer.open({
				type: 2,
				skin: 'layui-layer-molv',
				content: '/kcManagerTeacher/teacherInfo',
				title: '教师基本信息表',
				resize:false,
				area: ['70%', '27%']
			})
		},
		KCInfo:function(year,month){
			console.log("年:"+year+" 月:"+month);
			window.location.href="/kcManagerTeacher/kcInfo?year="+year+"&month="+month;
			
		},
		search:function(){
			var limit=10;
			var currentPage = 1;
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime=="" || endTime==""){
				failureMsg("开始日期和结束日期不能为空！");
				return false;
			}
			if(startTime > endTime){
                failureMsg("开始日期不能大于结束日期！");
                return false;
			}
			var data={
				limit : limit,
				currentPage :currentPage,
				startTime:startTime,
				endTime:endTime
			}
			$.get("/kcManagerTeacher/getProductName",function(resp){
				vm.productName = resp.data;
			})
			$.get("/kcManagerTeacher/getKCData",data,function(resp){
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
		},
		exportExcel:function(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime=="" || endTime==""){
				failureMsg("搜索条件不能为空!");
				return false;
			}
			window.location.href="/kcManagerTeacher/exportTeacherKC?startTime="+startTime+"&endTime="+endTime;
		}
	}
})
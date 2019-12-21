/**
 * 课酬管理-课酬统计
 * */
var laypage;
layui.use(['laydate','laypage'],function(){
	var laydate = layui.laydate;
	laypage = layui.laypage;
})
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		year:'',
		month:''
	},
	watch:{
		year:function(year){
			$.get("/kcManagerTeacher/getKCInfo?year="+vm.year+"&month="+vm.month,function(resp){
				vm.kcInfo = resp.data;
				//执行一个laypage实例
				laypage.render({
					elem: 'jqPager', //元素id
					layout: ['prev', 'page', 'next', 'skip', 'count'],
					limit:limit,
					count:vm.kcInfo.count,
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
	},
	
	methods:{
		search:function(){
			var projectName = $("#projectName").val();
			var data={
				"year":vm.year,
				"month":vm.month,
				"projectName":projectName
			}
			$.get("/kcManagerTeacher/getKCInfo",data,function(resp){
				vm.kcInfo = resp.data;
				var limit=10;
				var currentPage = 1;
				laypage.render({
					elem: 'jqPager', //元素id
					layout: ['prev', 'page', 'next', 'skip', 'count'],
					limit:limit,
					count:vm.kcInfo.count,
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
			var projectName = $("#projectName").val();
			window.location.href="/kcManagerTeacher/exportTeacherKCInfo?year="+vm.year+"&month="+vm.month+"&projectName="+projectName;
		}
	}
})


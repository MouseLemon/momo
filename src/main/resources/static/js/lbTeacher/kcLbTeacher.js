/**
 * 教师课酬类别
 * */
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		productName:[]
	},
	/*created:function(){
		$.get("/teacherKc/getTeacherType",function(resp){
			vm.kcInfo = resp.data;
			
		})
		$.get("/teacherKc/getProductName ",function(resp){
			vm.productName = resp.data;
			console.log("項目"+resp.data);
		})
	},*/
	methods:{
		search:function(){
			var yearAndMonth = $("#yearAndMonth").val();
			var teacherType = $("#teacherType").val();
			if(yearAndMonth =="" || yearAndMonth==null){
				failureMsg("请选择时间段！");
				return false;
			}
			var data={
				yearAndMonth:yearAndMonth,
				teacherType:teacherType
			}
			$.get("/teacherKc/getProductName ",function(resp){
				vm.productName = resp.data;
				console.log("項目"+resp.data);
			})
			$.get("/teacherKc/getTeacherType",data,function(resp){
				vm.kcInfo = resp.data;
				/*laypage.render({
					elem: 'Type', //元素id
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
				});*/
			})
		}
	}
})
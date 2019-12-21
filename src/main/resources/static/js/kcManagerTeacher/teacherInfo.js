/**
 * 课酬管理-课酬统计
 * */
var vm = new Vue({
	el:"#app",
	data:{
		teacherInfo:[],
		teacherCode:''
	},
	watch:{
		teacherCode:function(){
			$.get("/kcManagerTeacher/getTeacherInfo",{"teacherCode":vm.teacherCode},function(resp){
				vm.teacherInfo = resp;
			})
		}
	},
	methods:{
		
	}
})
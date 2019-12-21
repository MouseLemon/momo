/**
 * 统计明细
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
		serialNumber:"",
		status:""
	},
	watch:{
		serialNumber:function(serialNumber){
			vm.search('teacherCode');
		}
	},
	methods:{
		search:function(selectParam){
			$.get("/kcManager/queryKcDetatils",{"serialNumber":this.serialNumber,status:this.status,selectParam:selectParam},function(resp){
				vm.kcInfo = resp;
			})
		},
		/*back:function(){
			window.history.go(-1);
		}*/
	}
})


/**
 * 统计明细
 * */
var laypage;
var table;
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[],
		organizeCode:'',
		type:'',
        endTime:'',
		startTime:''
	},
	/*created:function(){
		console.log("1111"+vm.projectId)
	},*/
	watch:{
		organizeCode:function(){
			var data = {
				"organizeCode":vm.organizeCode,
				"type":vm.type,
				"startTime":vm.startTime,
				"endTime":vm.endTime
			};
			console.log(data);
			$.get("/teacherKc/getDepartDetail",data,function(resp){
				vm.kcInfo = resp;
			})
		}
	},
	methods:{
		/*search:function(){
			$.get("/teacherKc/getDepartDetail",{"depName":this.depName,status:this.status,selectParam:selectParam},function(resp){
				vm.kcInfo = resp;
			})
		}*/
	}
})
layui.use(['laydate','table','laypage'],function(){
	var laydate = layui.laydate;
	laypage = layui.laypage;
	table = layui.table;	
	//监听工具条
    /*table.on('tool(kcWdDepartment)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        alert(111)
        if (layEvent === 'detail') {//展示明细数据
          alert("明细")
        }
    });*/
    
})

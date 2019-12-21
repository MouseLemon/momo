/**
 * 统计明细
 * */
var vm = new Vue({
	el:"#app",
	data:{
		kcInfo:[]
       /* onlyDeduct:'1',
		deduct:"1"*/
	},
	/*created:function(){
		console.log("1111"+vm.projectId)
	},*/
    created: function () {
		$.get("/kcManager/getResearchKc",function (resp) {
            vm.kcInfo = resp;
        })
    },
	/*watch:{
        onlyDeduct:function (a) {
			console.log(a)
        },
		deduct:function (a) {
            console.log(a)
        },
	},*/
	methods:{
		search:function(){
			var depName = $("#depName").val();
			var projectName = $("#projectName").val();
			var teacherName = $("#teacherName").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var teacherType = $("#teacherType").val();
            var flag1 = document.getElementById("onlyDeduct").checked;
            var flag2 = document.getElementById("deduct").checked;
			var data = {
				"depName":depName,
				"projectName":projectName,
				"teacherName":teacherName,
				"startTime":startTime,
				"endTime":endTime,
				"teacherType":teacherType
			};
			if(flag1){
                data["onlyDeduct"] = "1";
			}
			if(flag2){
                data["deduct"] = "1";
			}
			$.get("/kcManager/getResearchKc",data,function(resp){
				vm.kcInfo = resp;
			})
		}
	}
})


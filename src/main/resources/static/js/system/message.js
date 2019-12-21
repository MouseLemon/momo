var vm = new Vue({
	el:"#app",
	data:{
		messageList:[]
	},
	created:function(){
		$.get("/message/getCustomMessage",function(resp){
			vm.messageList = resp.data;
			console.log(vm.messageList);
		})
	},
	methods:{
		save:function(id,type){
			var content = $("#"+id).val();
			var data = {
				"content":content,
				"id":id,
				"type":type
			}
			var success = function(resp){
				if(resp.result == 'OK'){
					successMsg("保存成功!");
				}else{
					failureMsg(resp.message);
				}
			}
			$.get("/message/updateMessageContent",data,success);
			
		}
	}
})
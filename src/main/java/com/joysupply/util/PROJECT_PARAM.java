package com.joysupply.util;

public enum PROJECT_PARAM {
	
	PROJECTNAME("项目名称"),
	LANGUAGE("语种"),
	DEPNAME("项目部名称"),
	PROJECTDESC("项目说明"),
	STARTTIME("开始时间"),
	ENDTIME("结束时间"),
	COUNTHOUR("课时总数"),
	STARTPERSONCOUNT("启动人数"),
	RETURNFEECOUNT("退费人数"),
	JOINCLASSCOUNT("插班人数"),
	CURRENTCOUNT("当前人数"),
	FEETYPE("收费方式"),
	FEEHOUR("学时学费"),
	FEESINGLE("单人学费"),
	STARTFEE("启动学费"),
	ADDFEE("增加学费"),
	SUBFEE("减少学费"),
	CURRENTINCOME("当前收入"),
	KCEXPEND("课酬支出"),
	HARDWAREEXPEND("硬件支出"),
	MANAGEEXPEND("管理支出"),
	ADEXPEND("广告支出"),
	AGENTEXPEND("代理支出"),
	TOTALINCOME("总收益"),
	PROJECTTYPE("项目类型");

	private String projectType; //项目类型

	private String name;
	
	PROJECT_PARAM(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
    public static boolean contains(String type){
        for(PROJECT_PARAM typeEnum : PROJECT_PARAM.values()){
            if(typeEnum.name().equals(type)){
                return true;
            }
        }
        return false;
    }

}

package com.joysupply.entity;

/**
 * 组织结构类型
 * @author Administrator
 *
 */
public class OrganizeType {
	private String typeCode;
	private String typeName;
	public OrganizeType(){
		
	}
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "OrganizeType [typeCode=" + typeCode + ", typeName=" + typeName + "]";
	}
	
}

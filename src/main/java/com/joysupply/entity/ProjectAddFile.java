package com.joysupply.entity;

public class ProjectAddFile {
	
	public String rowId;
	public String projectId;
	public String addName;
	public String uploader;
	public String uploadTime;
	public String url;
	public ProjectAddFile() {}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getUploader() {
		return uploader;
	}
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ProjectAddFile [rowId=" + rowId + ", projectId=" + projectId + ", addName=" + addName + ", uploader="
				+ uploader + ", uploadTime=" + uploadTime + ", url=" + url + "]";
	}
	
}

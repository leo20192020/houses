package com.git.house.common.page;

public class PageParams {

	private static final Integer PAGE_SIZE=5;
	private Integer pageSize;
	private Integer pageNum;
	private Integer offset;
	private Integer limit;
	
	
	/*
	 * pageNum and pageSize got from frontend
	 * offset and limit used in query from database
	 */
	public PageParams(Integer pageSize, Integer pageNum) { //pageNum seems like offset but NOT 100%
		this.pageNum=pageNum;
		this.pageSize=pageSize;
		this.offset=pageSize*(pageNum-1);  //data in database starts from 0
		this.limit=pageSize;  //get number 
	}

	public static PageParams build(Integer pageSize,Integer pageNum) {
		if(pageSize==null) {
			pageSize=PAGE_SIZE;
		}
		if(pageNum==null) {
			pageNum=1; //default to show first page
		}
		return new PageParams(pageSize,pageNum);
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	
	
	
	
	
}

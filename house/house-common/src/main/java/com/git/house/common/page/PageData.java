package com.git.house.common.page;

import java.util.List;

public class PageData <T>{

	/* listing.ftl
	 * <@common.paging ps.pagination/>
	 * ps.list as house
	 */
	private List<T> list;
	private Pagination pagination;
	public static <T> PageData<T> buildPage(List<T> list,Long count,Integer pageSize
			,Integer pageNum){
		Pagination pagination=new Pagination(pageSize,pageNum,count);
		return new PageData<>(list,pagination);
	}
	
	
	
	
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	public PageData(List<T> list, Pagination pagination) {
		super();
		this.list = list;
		this.pagination = pagination;
	}
	
	
	
}

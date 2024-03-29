package com.git.house.common.page;

import java.util.List;

import com.google.common.collect.Lists;

public class Pagination {

	/*
	 * common.ftl
	 *  <#if pagination.pageNum==page>
	 * <li><a href="javascript:void(0)"  onclick="nextPage(${page},${pagination.pageSize})">${page}</a></li>
	 *   
	 */
	private int pageNum;
	private int pageSize;
	private long totalCount;
	private List<Integer> pages=Lists.newArrayList();
	
	public Pagination(Integer pageSize,Integer pageNum,Long totalCount) {
		this.pageNum=pageNum;
		this.pageSize=pageSize;
		this.totalCount=totalCount;
		for(int i=1;i<=pageNum;i++) {
			pages.add(i);
		}
		Long pageCount=totalCount/pageSize+(totalCount/pageSize==0?0:1);
		if(pageCount>pageNum) {
			for(int i=pageNum+1;i<=pageCount;i++) {
				pages.add(i);
			}
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}
	
	
	
}

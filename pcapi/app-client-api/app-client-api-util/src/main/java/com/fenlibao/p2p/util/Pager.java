package com.fenlibao.p2p.util;

import java.util.List;

/**
 * 分页工具类
 * @param <T>
 */
public class Pager<T> {

	private List<T> list; //记录结果集
	
	private int total = 0; // 总记录数
	
	private int limit = 10; // 每页显示记录数
	
	private int pages = 1; // 总页数
	
	private int pageNumber = 1; // 当前页
	
	private int start = 0;//起始记录数
	
	public Pager(int total, int pageNumber) {
        init(total, pageNumber, limit);
    }
     
    public Pager(int total, int pageNumber, int limit) {
        init(total, pageNumber, limit);
    }
    
    private void init(int total, int pageNumber, int limit){
        //设置基本参数
        this.total=total;
        this.limit=limit;
        this.pages=(this.total-1)/this.limit+1;
        if(pageNumber<1){
            this.pageNumber=1;
        }else if(pageNumber>this.pages){
            this.pageNumber=this.pages;
        }else{
            this.pageNumber=pageNumber;
        }
        this.start=limit*(pageNumber-1);
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
 
    /**
     * 得到当前页的内容
     */
    public List<T> getList() {
        return list;
    }
 
    /**
     * 得到记录总数
     */
    public int getTotal() {
        return total;
    }
 
    /**
     * 得到每页显示多少条记录
     */
    public int getLimit() {
        return limit;
    }
 
    /**
     * 得到页面总数
     */
    public int getPages() {
        return pages;
    }
 
    /**
     * 得到当前页号
     */
    public int getPageNumber() {
        return pageNumber;
    }
    
    /**
     * 获取起始条数
     */
    public int getStart(){
    	return start;
    }
}

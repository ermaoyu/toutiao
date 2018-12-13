package com.maomaoyu.toutiao.util;

import com.sun.org.apache.regexp.internal.RE;

import java.util.List;

/**
 * maomaoyu    2018/12/12_16:57
 **/
public class PageBean<T> {
    private Integer id;//用来保存用户Id
    private String strId;
    private List<T> pageDate;
    private Integer curPage = Integer.valueOf(1);   //当前页数,默认第一页
    private Integer pageSize = Integer.valueOf(5);  //每页的数量,默认5条
    private Integer totalCount;                     //数据库中总行数数

    /**
     *  获取总页数
     * */
    public int getPageCount(){
        if (this.totalCount.intValue() % this.pageSize.intValue() == 0){
            return  this.totalCount.intValue() / this.pageSize.intValue();
        }else {
            return this.totalCount.intValue() / this.pageSize.intValue() + 1;
        }
    }

    public PageBean() {
    }

    /**
     *  判断是否为第一页
     * */
    public boolean isFirst(){
        return (this.curPage.intValue() == 1 || this.totalCount.intValue() == 0);
    }

    /**
     *     判断是否为最后一页
     * */
    public boolean isLast(){
        return (this.totalCount.intValue() == 0 || this.curPage.intValue() >= getPageCount());
    }

    /**
     *  判断是否有下一页
     * */
    public boolean isHastNext(){
        return this.curPage.intValue() < getPageCount();
    }


    public Integer getNextPage(){
        if (this.curPage.intValue() >= getPageCount()){
            return Integer.valueOf(getPageCount());
        }
        return Integer.valueOf(this.curPage.intValue() + 1);
    }

    /**
     *      判断是否有上一页
     * */
    public Integer getPrevPage(){
        if (this.curPage.intValue() <= 1){
            return Integer.valueOf(1);
        }
        return Integer.valueOf(this.curPage.intValue() - 1);
    }

    public List<T> getPageDate() {
        return pageDate;
    }

    public void setPageDate(List<T> pageDate) {
        this.pageDate = pageDate;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }
}

package cn.bixin.sona.common.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinwei
 */
@Description("通用的分页结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 5305667725145421839L;

    @Description("获取的列表数量")
    private Long count;

    @Description("是否到了最后一页")
    private Boolean end;

    @Description("分页结果列表")
    private List<T> list;

    @Description("游标标识")
    private String anchor;

    @Description("列表为空的提示字符串")
    private String emptyMsg;

    public static <T> PageResult<T> newPageResult(List<T> list, boolean end) {
        PageResult<T> result = new PageResult<>();
        result.setEnd(end);
        result.setList(list);
        return result;
    }

    public static <T> PageResult<T> newPageResult(List<T> list, boolean end, Long count, String anchor) {
        PageResult<T> result = new PageResult<>();
        result.setEnd(end);
        result.setList(list);
        result.setCount(count);
        result.setAnchor(anchor);
        return result;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getEmptyMsg() {
        return emptyMsg;
    }

    public void setEmptyMsg(String emptyMsg) {
        this.emptyMsg = emptyMsg;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "count=" + count +
                ", end=" + end +
                ", list=" + list +
                ", anchor='" + anchor + '\'' +
                ", emptyMsg='" + emptyMsg + '\'' +
                '}';
    }
}
package com.siti.system.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zyw on 2017/12/7.
 * 数据字典(表类)
 */
public final class TableVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 表 名
     */
    private String table_name;
    /**
     * 数据量
     */
    private String table_rows;
    /**
     * 自增量当前值
     */
    private String auto_increment;
    /**
     * 表 描述
     */
    private String table_comment;
    /**
     * 表的所有列
     */
    private List<ColumnVo> columns;

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_rows() {
        return table_rows;
    }

    public void setTable_rows(String table_rows) {
        this.table_rows = table_rows;
    }

    public String getAuto_increment() {
        return auto_increment;
    }

    public void setAuto_increment(String auto_increment) {
        this.auto_increment = auto_increment;
    }

    public String getTable_comment() {
        return table_comment;
    }

    public void setTable_comment(String table_comment) {
        this.table_comment = table_comment;
    }

    public List<ColumnVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnVo> columns) {
        this.columns = columns;
    }

}

package com.siti.system.vo;

import java.io.Serializable;

/**
 * Created by zyw on 2017/12/7.
 * 数据字典(字段类)
 */
public class ColumnVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 字段 名
     */
    private String column_name;
    /**
     * 数据类型
     */
    private String column_type;
    /**
     * 是否为空
     * YES 是
     * NO 否
     */
    private String is_nullable;
    /**
     * 约束类型
     * PRIMARY KEY 主键
     * FOREIGN KEY 外键
     * UNIQUE 唯一
     */
    private String constraint_type;
    /**
     * 字段默认值
     */
    private String column_default;
    /**
     * 字段 描述
     */
    private String column_comment;
    /**
     * 外键关联表
     */
    private String referenced_table_name;
    /**
     * 外键关联表的字段
     */
    private String referenced_column_name;

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getIs_nullable() {
        return is_nullable;
    }

    public void setIs_nullable(String is_nullable) {
        this.is_nullable = is_nullable;
    }

    public String getConstraint_type() {
        return constraint_type;
    }

    public void setConstraint_type(String constraint_type) {
        this.constraint_type = constraint_type;
    }

    public String getColumn_default() {
        return column_default;
    }

    public void setColumn_default(String column_default) {
        this.column_default = column_default;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    public String getReferenced_table_name() {
        return referenced_table_name;
    }

    public void setReferenced_table_name(String referenced_table_name) {
        this.referenced_table_name = referenced_table_name;
    }

    public String getReferenced_column_name() {
        return referenced_column_name;
    }

    public void setReferenced_column_name(String referenced_column_name) {
        this.referenced_column_name = referenced_column_name;
    }

}

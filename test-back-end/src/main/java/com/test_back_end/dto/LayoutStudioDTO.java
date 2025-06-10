package com.test_back_end.dto;

import com.test_back_end.enums.ChairStatus;

public class LayoutStudioDTO {

    private Long id;
    private Integer columnLayout;
    private Integer rowLayout;
    private ChairStatus status;
    private String chairNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getColumnLayout() {
        return columnLayout;
    }

    public void setColumnLayout(Integer columnLayout) {
        this.columnLayout = columnLayout;
    }

    public Integer getRowLayout() {
        return rowLayout;
    }

    public void setRowLayout(Integer rowLayout) {
        this.rowLayout = rowLayout;
    }

    public ChairStatus getStatus() {
        return status;
    }

    public void setStatus(ChairStatus status) {
        this.status = status;
    }

    public String getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(String chairNumber) {
        this.chairNumber = chairNumber;
    }
}

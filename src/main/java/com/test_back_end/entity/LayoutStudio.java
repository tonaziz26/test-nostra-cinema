package com.test_back_end.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "layout_studio")
public class LayoutStudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_layout", nullable = false)
    private Integer row;

    @Column(name = "column_layout", nullable = false)
    private Integer column;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "chair_number")
    private String chairNumber;

    @JoinColumn(name = "studio_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(String chairNumber) {
        this.chairNumber = chairNumber;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }
}

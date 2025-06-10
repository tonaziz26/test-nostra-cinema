package com.test_back_end.entity;

import com.test_back_end.enums.ChairStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "layout_studio")
public class LayoutStudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x_layout", nullable = false)
    private Integer xLayout;

    @Column(name = "y_layout", nullable = false)
    private Integer yLayout;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChairStatus status;

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

    public Integer getXLayout() {
        return xLayout;
    }

    public void setXLayout(Integer row) {
        this.xLayout = row;
    }

    public Integer getYLayout() {
        return yLayout;
    }

    public void setYLayout(Integer column) {
        this.yLayout = column;
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

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }
}

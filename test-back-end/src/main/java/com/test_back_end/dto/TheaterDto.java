package com.test_back_end.dto;

import java.util.List;
import java.util.Objects;

public class TheaterDto  {

    private Long id;
    private String name;
    private String code;
    private String address;

    List<StudioSessionDTO> studiosSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<StudioSessionDTO> getStudiosSessions() {
        return studiosSessions;
    }

    public void setStudiosSessions(List<StudioSessionDTO> studiosSessions) {
        this.studiosSessions = studiosSessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheaterDto that = (TheaterDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

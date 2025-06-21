package com.test_back_end.dto;

public class MovieDTO {
    private Long id;
    private String name;

    private String urlImage;
    
    public MovieDTO() {
    }
    
    public MovieDTO(Long id, String name, String urlImage) {
        this.id = id;
        this.name = name;
        this.urlImage = urlImage;
    }

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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

package com.test_back_end.dto;

public class MovieDTO {
    private Long id;
    private String title;

    private String urlImage;
    
    public MovieDTO() {
    }
    
    public MovieDTO(Long id, String title, String urlImage) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

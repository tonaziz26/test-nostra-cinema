package com.test_back_end.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "studio_session")
public class StudioSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "studio_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;


    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price" , nullable = false)
    private BigDecimal price;

    @Column(name = "additional_price")
    private BigDecimal additionalPrice = BigDecimal.ZERO;

    @ManyToMany
    @JoinTable(
        name = "session_movie",
        joinColumns = @JoinColumn(name = "studio_session_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> movies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getStudioSessions().add(this);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getStudioSessions().remove(this);
    }
}

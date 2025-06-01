package com.test_back_end.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "session_movie")
public class SessionMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "studio_session_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudioSession studioSession;

    @JoinColumn(name = "movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudioSession getStudioSession() {
        return studioSession;
    }

    public void setStudioSession(StudioSession studioSession) {
        this.studioSession = studioSession;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

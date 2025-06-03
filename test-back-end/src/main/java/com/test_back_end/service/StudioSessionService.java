package com.test_back_end.service;

import com.test_back_end.dto.StudioSessionDTO;
import com.test_back_end.dto.TheaterDto;
import com.test_back_end.entity.StudioSession;
import com.test_back_end.entity.Theater;
import com.test_back_end.entity.sql_response.StudioSessionSQL;
import com.test_back_end.repository.StudioSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudioSessionService {

    private final StudioSessionRepository studioSessionRepository;

    @Autowired
    public StudioSessionService(StudioSessionRepository studioSessionRepository) {
        this.studioSessionRepository = studioSessionRepository;
    }

    public Set<TheaterDto> getSessionList(Long cityId, Long movieId, Long dateStr) {
        LocalDate localDate = Instant.ofEpochMilli(dateStr)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        
        List<StudioSessionSQL> sessions = studioSessionRepository.findSessionByCityIdAndDateRange(cityId, movieId, localDate);
        return mapToTheaterDtoSet(sessions, localDate);
    }

    private Set<TheaterDto> mapToTheaterDtoSet(List<StudioSessionSQL> sessions, LocalDate date) {

        Set<TheaterDto> theaterDtos = sessions.stream()
                .map(sql -> {
                    TheaterDto dto = new TheaterDto();
                    dto.setId(sql.getTheaterId());
                    dto.setName(sql.getTheaterName());
                    dto.setCode(sql.getTheaterCode());
                    dto.setAddress(sql.getTheaterAddress());
                    return dto;
                })
                .collect(Collectors.toSet());

        theaterDtos.forEach(theaterDto -> {
            theaterDto.setStudiosSessions(sessions.stream().filter(
                    session -> session.getTheaterId().equals(theaterDto.getId()))
                    .map(session -> {
                        StudioSessionDTO dto = new StudioSessionDTO();
                        dto.setId(session.getStudioSessionId());
                        dto.setStartTime(session.getStartTime());
                        dto.setPrice(isWeekend(date) ? session.getPrice().add(session.getAdditionalPrice()) : session.getPrice());
                        dto.setStudioNumber(session.getStudioNumber());
                        return dto;
                    }).collect(Collectors.toList()));
        });

        return theaterDtos;
    }


    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}

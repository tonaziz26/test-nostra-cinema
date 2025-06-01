package com.test_back_end.service;

import com.test_back_end.dto.StudioSessionDTO;
import com.test_back_end.dto.TheaterDto;
import com.test_back_end.entity.StudioSession;
import com.test_back_end.entity.Theater;
import com.test_back_end.repository.StudioSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public Set<TheaterDto> getSessionList(Long cityId, Long movieId, String dateStr) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format. Use ISO format (yyyy-MM-dd)", dateStr, e.getErrorIndex());
        }
        
        List<StudioSession> sessions = studioSessionRepository.findSessionByCityIdAndDateRange(cityId, movieId, localDate);
        return mapToTheaterDtoSet(sessions, localDate);
    }

    private Set<TheaterDto> mapToTheaterDtoSet(List<StudioSession> sessions, LocalDate date) {

        Set<TheaterDto> theaterDtos = sessions.stream().map(session -> {
            Theater theater = session.getStudio().getTheater();

            TheaterDto dto = new TheaterDto();
            dto.setId(theater.getId());
            dto.setName(theater.getName());
            dto.setCode(theater.getCode());
            dto.setAddress(theater.getAddress());
            return dto;
        }).collect(Collectors.toSet());

        theaterDtos.forEach(theaterDto -> {
            theaterDto.setStudiosSessions(sessions.stream().filter(
                    session -> session.getStudio().getTheater().getId().equals(theaterDto.getId()))
                    .map(session -> {
                        StudioSessionDTO dto = new StudioSessionDTO();
                        dto.setId(session.getId());
                        dto.setStartTime(session.getStartTime());
                        dto.setPrice(isWeekend(date) ? session.getPrice().add(session.getAdditionalPrice()) : session.getPrice());
                        dto.setStudioNumber(session.getStudio().getNumber());
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

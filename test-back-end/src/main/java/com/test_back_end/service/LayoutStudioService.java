package com.test_back_end.service;

import com.test_back_end.dto.LayoutStudioDTO;
import com.test_back_end.entity.LayoutStudio;
import com.test_back_end.repository.LayoutStudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LayoutStudioService {

    @Autowired
    private LayoutStudioRepository layoutStudioRepository;


    public List<LayoutStudioDTO> getLayoutStudio(Long sessionMovieId) {

        List<LayoutStudio> layoutStudios = layoutStudioRepository.findByStudioSessionId(sessionMovieId);

        return layoutStudios.stream()
                .map(ls -> {
                    LayoutStudioDTO dto = new LayoutStudioDTO();
                    dto.setId(ls.getId());
                    dto.setRowLayout(ls.getYLayout());
                    dto.setColumnLayout(ls.getXLayout());
                    dto.setStatus(ls.getStatus());
                    dto.setChairNumber(ls.getChairNumber());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}

package com.test_back_end.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.dto.LayoutStudioDTO;
import com.test_back_end.entity.LayoutStudio;
import com.test_back_end.entity.Transaction;
import com.test_back_end.enums.ChairStatus;
import com.test_back_end.repository.LayoutStudioRepository;
import com.test_back_end.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LayoutStudioService {

    @Autowired
    private LayoutStudioRepository layoutStudioRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ObjectMapper objectMapper;


    public List<LayoutStudioDTO> getLayoutStudio(Long studioSessionId, Long bookingDate) {

        List<LayoutStudio> layoutStudios = layoutStudioRepository.findByStudioSessionId(studioSessionId);

        List<Transaction> transactions = transactionRepository.findByPaymentBookingDateAndSessionId(Instant.ofEpochMilli(bookingDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), studioSessionId);

        List<LayoutStudioDTO> layoutStudioDTOs = layoutStudios.stream()
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

        if (!transactions.isEmpty()) {
            transactions.forEach(t -> {
                layoutStudioDTOs.stream()
                        .filter(ls -> null != ls.getChairNumber() && ls.getChairNumber().equals(t.getChairNumber()))
                        .forEach(ls -> ls.setStatus(ChairStatus.BOOKED));
            });
        }

        return layoutStudioDTOs;
    }

}

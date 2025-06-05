package com.test_back_end.service;

import com.test_back_end.entity.LayoutStudio;
import com.test_back_end.entity.Transaction;
import com.test_back_end.repository.LayoutStudioRepository;
import com.test_back_end.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
public class LayoutStudioService {

    @Autowired
    private LayoutStudioRepository layoutStudioRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public List<LayoutStudio> getLayoutStudio(Long studioSessionId, Long bookingDate) {

        List<LayoutStudio> layoutStudios = layoutStudioRepository.findByStudioSessionId(studioSessionId);

        List<Transaction> transactions = transactionRepository.findByPaymentBookingDate(Instant.ofEpochMilli(bookingDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        if (transactions != null) {
            transactions.forEach(t -> {
                layoutStudios.stream().filter(ls -> ls.getChairNumber().equals(t.getChairNumber()))
                        .forEach(ls -> ls.setType("BOOKED"));
            });
        }

        return layoutStudios;
    }

}

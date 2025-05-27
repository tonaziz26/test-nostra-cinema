package com.test_back_end.service;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.dto.PaymentDTO;
import com.test_back_end.entity.Payment;
import com.test_back_end.enums.PaymentStatus;
import com.test_back_end.repository.PaymentRepository;
import com.test_back_end.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    public PageResultDTO<PaymentDTO> getPaymentsByPaymentNumber(String paymentNumber, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Payment> paymentPage = paymentRepository.findByPaymentNumberContainingIgnoreCase(paymentNumber, pageable);
        
        List<PaymentDTO> paymentDTOs = paymentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResultDTO<>(paymentDTOs, paymentPage.getNumber(), paymentPage.getTotalElements());
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setSecureId(payment.getSecureId());
        dto.setPaymentNumber(payment.getPaymentNumber());
        dto.setStatus(payment.getStatus());

        if (payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT) && LocalDateTime.now().isAfter(payment.getExpiredTime())) {
            payment.setStatus(PaymentStatus.EXPIRED);
        }
        dto.setExpiredTime(payment.getExpiredTime());
        dto.setBookingDate(payment.getBookingDate());
        dto.setTotalPrice(payment.getTotalPrice());
        return dto;
    }
}

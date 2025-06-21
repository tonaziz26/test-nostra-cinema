package com.test_back_end.dto.request;

import com.test_back_end.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public class PaymentApprovalDTO {

    @NotNull(message = "Status is required")
    private PaymentStatus status;

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}

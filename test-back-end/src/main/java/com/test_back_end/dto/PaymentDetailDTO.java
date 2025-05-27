package com.test_back_end.dto;

import java.util.List;

public class PaymentDetailDTO extends PaymentDTO {

    private List<TransactionDTO> transactions;

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}

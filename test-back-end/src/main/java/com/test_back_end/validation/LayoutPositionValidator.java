package com.test_back_end.validation;

import com.test_back_end.dto.LayoutStudioDTO;
import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.enums.ChairStatus;
import com.test_back_end.service.LayoutStudioService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LayoutPositionValidator implements ConstraintValidator<LayoutPosition, PaymentRequestDTO> {

    @Autowired
    private LayoutStudioService layoutStudioService;

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequestDTO, ConstraintValidatorContext context) {

        List<LayoutStudioDTO> layoutStudios = layoutStudioService.getLayoutStudio(
                paymentRequestDTO.getSessionMovieId());

        paymentRequestDTO.getTransactions().sort(
                Comparator.comparing(
                        TransactionRequestDTO::getOrderNumber,
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
        );

        // validation left side

        String chairNumberLeft = paymentRequestDTO.getTransactions().get(0).getChairNumber();
        Optional<LayoutStudioDTO> currentChairLeft = layoutStudios.stream()
                .filter(ls -> chairNumberLeft.equals(ls.getChairNumber()))
                .findFirst();

        if (currentChairLeft.isEmpty()) {
            return true;
        }

        Integer currentColumnLeft = currentChairLeft.get().getColumnLayout();
        Integer currentRowLeft = currentChairLeft.get().getRowLayout();

        Optional<LayoutStudioDTO> leftSeat = findChairByPosition(layoutStudios, currentRowLeft, currentColumnLeft - 1);
        Optional<LayoutStudioDTO> twoLeftSeat = findChairByPosition(layoutStudios, currentRowLeft, currentColumnLeft - 2);
        if (leftSeat.isPresent() && ChairStatus.AVAILABLE.equals(leftSeat.get().getStatus()) &&
                twoLeftSeat.isPresent() && ChairStatus.BOOKED.equals(twoLeftSeat.get().getStatus())) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot book seat " + chairNumberLeft + " because it would leave a single empty seat next to a booked seat")
                    .addConstraintViolation();
            return false;
        }

        // validation right side

        String chairNumberRight = paymentRequestDTO.getTransactions().get(paymentRequestDTO.getTransactions().size() - 1).getChairNumber();


        Optional<LayoutStudioDTO> currentChairRight = layoutStudios.stream()
                .filter(ls -> chairNumberRight.equals(ls.getChairNumber()))
                .findFirst();

        if (currentChairRight.isEmpty()) {
            return true;
        }

        Integer currentColumnRight = currentChairRight.get().getColumnLayout();
        Integer currentRowRight = currentChairRight.get().getRowLayout();

        Optional<LayoutStudioDTO> rightSeat = findChairByPosition(layoutStudios, currentRowRight, currentColumnRight + 1);
        Optional<LayoutStudioDTO> twoRightSeat = findChairByPosition(layoutStudios, currentRowRight, currentColumnRight + 2);
        if (rightSeat.isPresent() && ChairStatus.AVAILABLE.equals(rightSeat.get().getStatus()) &&
                twoRightSeat.isPresent() && ChairStatus.BOOKED.equals(twoRightSeat.get().getStatus())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot book seat " + chairNumberRight + " because it would leave a single empty seat next to a booked seat")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private Optional<LayoutStudioDTO> findChairByPosition(List<LayoutStudioDTO> layoutStudios, Integer row, Integer column) {
        return layoutStudios.stream()
                .filter(ls -> row.equals(ls.getRowLayout()) && column.equals(ls.getColumnLayout()))
                .findFirst();
    }
}

package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateOrderStatusDTO {
    @NotBlank(message = "Le status ne peut pas être vide")
    @Pattern(
            regexp = "PENDING|VALIDATED|DELIVERED|CANCELED",
            message = "Le status doit être l'une des valeurs suivantes : PENDING, VALIDATED, DELIVERED, CANCELED"
    )
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderStatus getOrderStatusEnum() {
        return OrderStatus.valueOf(status);
    }
}

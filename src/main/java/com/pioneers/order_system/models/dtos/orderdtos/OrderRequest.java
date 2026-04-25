package com.pioneers.order_system.models.dtos.orderdtos;

import com.pioneers.order_system.models.enums.CustomerType;
import com.pioneers.order_system.models.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
public record OrderRequest (
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @NotBlank(message = "Customer name is required and cannot be empty")
    String customerName,
    @NotNull(message = "Customer type (VIP/NORMAL) is required")
    CustomerType customerType,
    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod,
    @NotEmpty(message = "Order must contain at least one product ID")
    List<@NotNull Long> productIds
)
{
}

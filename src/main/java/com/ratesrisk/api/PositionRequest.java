package com.ratesrisk.api;

import com.ratesrisk.domain.ProductType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PositionRequest(@NotBlank @Size(max=100) String tradeId, @NotBlank @Size(max=100) String desk, @NotBlank @Size(max=100) String productFamily,
 @NotNull ProductType productType, @NotNull @Digits(integer=14, fraction=6) BigDecimal quantity,
 @DecimalMin(value="0.01", inclusive=true) @Digits(integer=14, fraction=6) BigDecimal faceValue,
 @DecimalMin(value="0.01", inclusive=true) @Digits(integer=14, fraction=6) BigDecimal contractMultiplier,
 @NotNull @Digits(integer=14, fraction=6) BigDecimal previousPrice, @NotNull @Digits(integer=14, fraction=6) BigDecimal currentPrice) {
    public PositionRequest {
        if (quantity != null && quantity.signum() == 0) throw new IllegalArgumentException("quantity must not be zero");
        if (productType == ProductType.BOND && (faceValue == null || contractMultiplier != null))
            throw new IllegalArgumentException("BOND requires faceValue and must not include contractMultiplier");
        if (productType == ProductType.FUTURE && (contractMultiplier == null || faceValue != null))
            throw new IllegalArgumentException("FUTURE requires contractMultiplier and must not include faceValue");
    }
}

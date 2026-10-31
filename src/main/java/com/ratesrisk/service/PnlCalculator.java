package com.ratesrisk.service;

import com.ratesrisk.domain.Position;
import com.ratesrisk.domain.ProductType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class PnlCalculator {
    public BigDecimal calculate(Position p) {
        BigDecimal move = p.getCurrentPrice().subtract(p.getPreviousPrice());
        BigDecimal raw = switch (p.getProductType()) {
            case BOND -> p.getQuantity().multiply(p.getFaceValue()).multiply(move).divide(new BigDecimal("100"));
            case FUTURE -> p.getQuantity().multiply(p.getContractMultiplier()).multiply(move);
        };
        return raw.setScale(2, RoundingMode.HALF_UP);
    }
}

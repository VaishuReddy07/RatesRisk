package com.ratesrisk.api;

import com.ratesrisk.domain.Position;
import com.ratesrisk.service.PnlCalculator;
import com.ratesrisk.service.PositionService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class PositionController {
    private final PositionService service;
    private final PnlCalculator calculator;
    public PositionController(PositionService service, PnlCalculator calculator) { this.service=service; this.calculator=calculator; }
    @PostMapping("/positions") public PositionView create(@Valid @RequestBody PositionRequest r) {
        return PositionView.from(service.create(new Position(r.tradeId(),r.desk(),r.productFamily(),r.productType(),r.quantity(),r.faceValue(),r.contractMultiplier(),r.previousPrice(),r.currentPrice())));
    }
    @GetMapping("/positions") public List<PositionView> positions(){return service.all().stream().map(PositionView::from).toList();}
    @GetMapping("/pnl") public Map<String,Object> pnl() {
        var results = service.all().stream().map(p -> new PnlRow(p.getId(),p.getTradeId(),p.getDesk(),p.getProductFamily(),p.getProductType().name(),p.getQuantity(),p.getPreviousPrice(),p.getCurrentPrice(),calculator.calculate(p))).toList();
        Map<String,BigDecimal> byDesk=new TreeMap<>(), byFamily=new TreeMap<>();
        for (var r:results) { byDesk.merge(r.desk(),r.pnl(),BigDecimal::add); byFamily.merge(r.productFamily(),r.pnl(),BigDecimal::add); }
        return Map.of("positions",results,"byDesk",byDesk,"byProductFamily",byFamily,"total",results.stream().map(PnlRow::pnl).reduce(BigDecimal.ZERO,BigDecimal::add));
    }
    private record PositionView(Long id,String tradeId,String desk,String productFamily,String productType,BigDecimal quantity,BigDecimal faceValue,BigDecimal contractMultiplier,BigDecimal previousPrice,BigDecimal currentPrice) {
        static PositionView from(Position p) { return new PositionView(p.getId(),p.getTradeId(),p.getDesk(),p.getProductFamily(),p.getProductType().name(),p.getQuantity(),p.getFaceValue(),p.getContractMultiplier(),p.getPreviousPrice(),p.getCurrentPrice()); }
    }
    private record PnlRow(Long id,String tradeId,String desk,String productFamily,String productType,BigDecimal quantity,BigDecimal previousPrice,BigDecimal currentPrice,BigDecimal pnl) {}
}

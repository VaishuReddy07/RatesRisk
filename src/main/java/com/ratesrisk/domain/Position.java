package com.ratesrisk.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="positions")
public class Position {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="trade_id", nullable=false, length=100) private String tradeId;
    @Column(nullable=false, length=100) private String desk;
    @Column(name="product_family", nullable=false, length=100) private String productFamily;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private ProductType productType;
    @Column(nullable=false, precision=20, scale=6) private BigDecimal quantity;
    @Column(name="face_value", precision=20, scale=6) private BigDecimal faceValue;
    @Column(name="contract_multiplier", precision=20, scale=6) private BigDecimal contractMultiplier;
    @Column(name="previous_price", nullable=false, precision=20, scale=6) private BigDecimal previousPrice;
    @Column(name="current_price", nullable=false, precision=20, scale=6) private BigDecimal currentPrice;
    protected Position() {}
    public Position(String tradeId, String desk, String productFamily, ProductType productType, BigDecimal quantity, BigDecimal faceValue, BigDecimal contractMultiplier, BigDecimal previousPrice, BigDecimal currentPrice) {
        this.tradeId=tradeId; this.desk=desk; this.productFamily=productFamily; this.productType=productType; this.quantity=quantity;
        this.faceValue=faceValue; this.contractMultiplier=contractMultiplier; this.previousPrice=previousPrice; this.currentPrice=currentPrice;
    }
    public Long getId(){return id;} public String getTradeId(){return tradeId;} public String getDesk(){return desk;} public String getProductFamily(){return productFamily;}
    public ProductType getProductType(){return productType;} public BigDecimal getQuantity(){return quantity;} public BigDecimal getFaceValue(){return faceValue;}
    public BigDecimal getContractMultiplier(){return contractMultiplier;} public BigDecimal getPreviousPrice(){return previousPrice;} public BigDecimal getCurrentPrice(){return currentPrice;}
}

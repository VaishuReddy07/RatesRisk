package com.ratesrisk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ratesrisk.domain.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PnlCalculatorTest {
    private final PnlCalculator calculator=new PnlCalculator();
    private Position bond(String q,String f,String old,String now){return new Position("B1","Rates","Government",ProductType.BOND,new BigDecimal(q),new BigDecimal(f),null,new BigDecimal(old),new BigDecimal(now));}
    private Position future(String q,String m,String old,String now){return new Position("F1","Rates","ShortRate",ProductType.FUTURE,new BigDecimal(q),null,new BigDecimal(m),new BigDecimal(old),new BigDecimal(now));}
    @Test void workedBondExample(){assertEquals(new BigDecimal("12.50"),calculator.calculate(bond("10","1000","99.50","99.625")));}
    @Test void workedFutureExample(){assertEquals(new BigDecimal("2500.00"),calculator.calculate(future("5","1000","98.00","98.50")));}
    @Test void shortPositionLosesWhenMarketRises(){assertEquals(new BigDecimal("-500.00"),calculator.calculate(future("-1","1000","98","98.5")));}
    @Test void negativeMarketMoveProducesLoss(){assertEquals(new BigDecimal("-10.00"),calculator.calculate(bond("10","1000","100","99.90")));}
    @Test void unchangedPriceHasZeroPnl(){assertEquals(new BigDecimal("0.00"),calculator.calculate(future("9","1000","98","98")));}
    @Test void halfUpRounding(){assertEquals(new BigDecimal("0.01"),calculator.calculate(future("1","1","0","0.005")));}
    @ParameterizedTest(name="future regression case {index}: price move {0}")
    @ValueSource(ints={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40})
    void fortyFormulaCases(int move) {
        var p=future("1","100","0",Integer.toString(move));
        assertEquals(new BigDecimal(move*100+".00"),calculator.calculate(p));
    }
}

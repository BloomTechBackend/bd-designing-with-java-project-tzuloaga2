package com.amazon.ata.cost;
import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;
import com.amazonaws.services.dynamodbv2.xspec.B;
import org.checkerframework.checker.units.qual.C;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class WeightedCostStrategy implements CostStrategy {
    MonetaryCostStrategy monetaryCostStrategy = new MonetaryCostStrategy();
    CarbonCostStrategy carbonCostStrategy = new CarbonCostStrategy();

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.monetaryCostStrategy = monetaryCostStrategy;
        this.carbonCostStrategy = carbonCostStrategy;
    }

    public WeightedCostStrategy() {
    }

    //mass and type
    //80% monetary and 20 carbon
    //mass times monetary cost strategy
    //that times .8 and save as variable
    //take packaging mass times  carbon strategy then .2 save and add all together

    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        BigDecimal weightedCost;
        BigDecimal monetaryCost;
        BigDecimal carbonCost;

        carbonCost = carbonCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.2));
        monetaryCost = monetaryCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.8));
        weightedCost = carbonCost.add(monetaryCost);
        return new ShipmentCost(shipmentOption, weightedCost);
    }
}





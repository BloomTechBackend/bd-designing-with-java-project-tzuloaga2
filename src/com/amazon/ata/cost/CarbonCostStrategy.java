package com.amazon.ata.cost;
        import com.amazon.ata.types.Material;
        import com.amazon.ata.types.Packaging;
        import com.amazon.ata.types.ShipmentCost;
        import com.amazon.ata.types.ShipmentOption;

        import java.math.BigDecimal;
        import java.util.HashMap;
        import java.util.Map;

/**
 * A strategy to calculate the cost of a ShipmentOption based on its carbon cost.
 */
public class CarbonCostStrategy implements CostStrategy {

    private final Map<Material, BigDecimal> carbonCostPerGram;

    /**
     * Initializes a CarbonCostStrategy.
     */
    public CarbonCostStrategy() {
        carbonCostPerGram = new HashMap<>();
        carbonCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(.017));
        carbonCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(.012));
    }


    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal materialCost = this.carbonCostPerGram.get(packaging.getMaterial());
        BigDecimal cost = packaging.getMass().multiply(materialCost);
        return new ShipmentCost(shipmentOption, cost);
    }
}


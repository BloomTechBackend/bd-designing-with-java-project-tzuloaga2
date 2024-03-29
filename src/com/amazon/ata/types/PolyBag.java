package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

public class PolyBag extends Packaging {
//added this note to push code because it wouldn't let me
    private BigDecimal volume;

    public PolyBag(Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }
    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getMass() {
       double mass = Math.ceil(Math.sqrt(volume.doubleValue()) * 0.6);
       return BigDecimal.valueOf(mass);
    }

    public boolean canFitItem(Item item) {
        return this.volume.compareTo(item.getHeight().multiply(item.getLength().multiply(item.getWidth()))) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PolyBag polyBag = (PolyBag) o;
        return getVolume().equals(polyBag.getVolume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVolume());
    }
 }

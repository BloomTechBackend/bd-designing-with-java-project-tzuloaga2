package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

public class Box extends Packaging {

    private final BigDecimal length;
    private final BigDecimal width;
    private final BigDecimal height;

    public Box(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public boolean canFitItem(Item item) {
        return this.length.compareTo(item.getLength()) > 0 &&
                this.width.compareTo(item.getWidth()) > 0 &&
                this.height.compareTo(item.getHeight()) > 0;
    }

    public BigDecimal getMass() {
        BigDecimal two = BigDecimal.valueOf(2);

//         For simplicity, we ignore overlapping flaps
        BigDecimal endsArea = length.multiply(width).multiply(two);
        BigDecimal shortSidesArea = length.multiply(height).multiply(two);
        BigDecimal longSidesArea = width.multiply(height).multiply(two);

        return endsArea.add(shortSidesArea).add(longSidesArea);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Box box = (Box) o;
        return getLength().equals(box.getLength()) && getWidth().equals(box.getWidth()) && getHeight().equals(box.getHeight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLength(), getWidth(), getHeight());
    }
}


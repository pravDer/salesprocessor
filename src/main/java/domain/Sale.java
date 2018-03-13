package domain;

public class Sale
{
    private String productType;
    private double price;

    public Sale(String productType, double price)
    {
        this.productType = productType;
        this.price = price;
    }

    public String getProductType()
    {
        return productType;
    }

    public double getPrice()
    {
        return price;
    }

    public void applyAdjustment(Sale adjustmentSale, String adjustmentType)
    {
        if(!SaleAdjustmentOperation.getValidAdjustmentOperations().contains(adjustmentType) ||
                !adjustmentSale.productType.equals(this.productType))
        {
            throw new UnsupportedOperationException("Adjustment is not valid");
        }
        this.price = SaleAdjustmentOperation.valueOf(adjustmentType).apply(this.price, adjustmentSale.price);;
    }
}

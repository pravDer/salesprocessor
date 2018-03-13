package domain;

public class MessageType3 extends AbstractMessage
{

    private String saleAdjustmentOperation;

    public MessageType3(Sale sale, String saleAdjustmentOperation)
    {
        super(sale);
        this.saleAdjustmentOperation = saleAdjustmentOperation;
    }

    public String getSaleAdjustmentOperation()
    {
        return saleAdjustmentOperation;
    }

    @Override
    public boolean hasAdjustment()
    {
        return true;
    }

    @Override
    public int numberOfSales()
    {
        throw new UnsupportedOperationException();
        //return 0;
    }

    @Override
    public String toString()
    {
        return String.format("Adjustment Type %s Made for value %f", this.saleAdjustmentOperation,
                this.getSale().getPrice());
    }

}

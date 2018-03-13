package domain;

public class MessageType2 extends AbstractMessage
{
    private int numberOfSales;

    public MessageType2(Sale sale, int numberOfSales)
    {
        super(sale);
        this.numberOfSales = numberOfSales;
    }

    @Override
    public boolean hasAdjustment()
    {
        return false;
    }

    @Override
    public int numberOfSales()
    {
        return this.numberOfSales;
    }
}

package domain;

public class MessageType1 extends AbstractMessage
{
    public MessageType1(Sale sale)
    {
       super(sale);
    }

    @Override
    public boolean hasAdjustment()
    {
        return false;
    }

    @Override
    public int numberOfSales()
    {
        return 1;
    }
}

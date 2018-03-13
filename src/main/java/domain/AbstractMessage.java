package domain;

public abstract class AbstractMessage implements Message
{
    private Sale sale;

    AbstractMessage(Sale sale)
    {
        this.sale = sale;
    }

    @Override
    public Sale getSale()
    {
        return sale;
    }
}

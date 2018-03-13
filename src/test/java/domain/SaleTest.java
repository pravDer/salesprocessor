package domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class SaleTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testApplyAdjustment()
    {
        Sale sale = new Sale("Apple", 2.5);
        Sale adjustmenSale = new Sale("Apple", 2);

        sale.applyAdjustment(adjustmenSale, SaleAdjustmentOperation.ADD.name());

        assertEquals(sale.getPrice(), 4.5, 0.005);
    }

    @Test
    public void testApplyAdjustmentForInvalidOperation()
    {
        Sale sale = new Sale("Apple", 2.5);
        Sale adjustmenSale = new Sale("Apple", 2);

        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Adjustment is not valid");

        sale.applyAdjustment(adjustmenSale, "RND");
    }
}

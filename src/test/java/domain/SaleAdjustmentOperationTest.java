package domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SaleAdjustmentOperationTest
{

    private static final double THRESHOLD = 0.005;

    @Test
    public void testAddOperation()
    {
        assertEquals("Unexpected Add Result",4.0, SaleAdjustmentOperation.ADD.apply(2.0,2.0), THRESHOLD);
    }

    @Test
    public void testSubOperation()
    {
        assertEquals("Unexpected Sub Result",3.0, SaleAdjustmentOperation.SUB.apply(5.0,2.0), THRESHOLD);
    }

    @Test
    public void testMultiplyOperation()
    {
        assertEquals("Unexpected Multiply Result",8.0, SaleAdjustmentOperation.MULTIPLY.apply(4.0,2.0), THRESHOLD);
    }
}

package app;

import domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SalesProcessorTest
{
    private SalesProcessorForTest salesProcessor;
    private static final double THRESHOLD = 0.005;

    @Before
    public void setup()
    {
        this.salesProcessor = new SalesProcessorForTest();
    }

    @After
    public void tearDown()
    {
        this.salesProcessor = null;
    }

    @Test
    public void testSaleProcessForMessageType1()
    {
        Message message1 = new MessageType1(new Sale("Apple", 2.0));
        salesProcessor.process(message1);

        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(), 1, salesProcessor.getNumberOfMessagesProcessed());
        assertEquals(ErrorMessage.ADJUSTMENT_COUNT_MISMATCH.toString(), 0, salesProcessor.getAdjustmentMessages().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),1, salesProcessor.getProductTypeToProcessedSales().size());
    }

    @Test
    public void testSaleProcessForMessageType2()
    {
        Message message2 = new MessageType2(new Sale("Apple", 2.0), 5);
        salesProcessor.process(message2);

        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(),1, salesProcessor.getNumberOfMessagesProcessed());
        assertEquals(ErrorMessage.ADJUSTMENT_COUNT_MISMATCH.toString(),0, salesProcessor.getAdjustmentMessages().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),1, salesProcessor.getProductTypeToProcessedSales().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),5, salesProcessor.getProductTypeToProcessedSales().get("Apple").size());
    }

    @Test
    public void testSaleProcessForMessageType3()
    {
        Message message2 = new MessageType2(new Sale("Apple", 2.0), 5);
        salesProcessor.process(message2);

        Message message3 = new MessageType3(new Sale("Apple", 1.0), SaleAdjustmentOperation.ADD.name());
        salesProcessor.process(message3);

        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(),2, salesProcessor.getNumberOfMessagesProcessed());
        assertEquals(ErrorMessage.ADJUSTMENT_COUNT_MISMATCH.toString(),1, salesProcessor.getAdjustmentMessages().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),1, salesProcessor.getProductTypeToProcessedSales().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),5, salesProcessor.getProductTypeToProcessedSales().get("Apple").size());
        assertEquals(3.0, salesProcessor.getProductTypeToProcessedSales().get("Apple").get(1).getPrice(), THRESHOLD);
    }

    @Test
    public void testAdjustmentOnlyImpactIntendedSale()
    {
        Message messageApple = new MessageType1(new Sale("Apple", 2.0));
        salesProcessor.process(messageApple);

        Message messageOrange = new MessageType1(new Sale("Orange", 3.0));
        salesProcessor.process(messageOrange);

        Message adjustAppleMessage = new MessageType3(new Sale("Apple", 2.0), SaleAdjustmentOperation.MULTIPLY.name());
        salesProcessor.process(adjustAppleMessage);

        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(),3, salesProcessor.getNumberOfMessagesProcessed());
        assertEquals(ErrorMessage.ADJUSTMENT_COUNT_MISMATCH.toString(),1, salesProcessor.getAdjustmentMessages().size());
        assertEquals(ErrorMessage.SALES_MISMATCH.toString(),2, salesProcessor.getProductTypeToProcessedSales().size());
        assertEquals(ErrorMessage.PRICE_MISMATCH.toString(),4.0, salesProcessor.getProductTypeToProcessedSales().get("Apple").get(0).getPrice(), THRESHOLD);
        assertEquals(ErrorMessage.PRICE_MISMATCH.toString(),3.0, salesProcessor.getProductTypeToProcessedSales().get("Orange").get(0).getPrice(), THRESHOLD);
    }

    @Test
    public void testSaleProcessForTenMessages()
    {
        String expectedMessage = "For productType Apple there are 50 sales made for total value of 450.000000\n";
        for(int i=0; i < 10 ; i++)
        {
            Message message2 = new MessageType2(new Sale("Apple", 2.0 * i), 5);
            salesProcessor.process(message2);
        }

        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(),10, salesProcessor.getNumberOfMessagesProcessed());
        String outputMessage = salesProcessor.checkAndGetSalesReportForEveryTenthMessage();
        assertEquals(ErrorMessage.LOG_MESSAGE_MISMATCH.toString(),expectedMessage, outputMessage);

    }

    @Test
    public void testSaleProcessForFiftyMessages()
    {
        String expectedMessage = "For productType Apple adjustments made are:\nAdjustment Type ADD Made for value 1.000000\n" +
                "Adjustment Type MULTIPLY Made for value 2.000000\n"+
                "For productType Orange adjustments made are:\nAdjustment Type MULTIPLY Made for value 2.000000\n";

        //Process 30 Sale messages on Apple
        for(int i=0; i < 29 ; i++)
        {
            Message message2 = new MessageType2(new Sale("Apple", 2.5), 5);
            salesProcessor.process(message2);
        }

        //Process Adjustment for Apple to ADD price 1
        Message adjustAppleMessage = new MessageType3(new Sale("Apple", 1.0), SaleAdjustmentOperation.ADD.name());
        salesProcessor.process(adjustAppleMessage);

        //Process Adjustment for Apple to multiply price by 2
        Message adjustAppleMessage2 = new MessageType3(new Sale("Apple", 2.0), SaleAdjustmentOperation.MULTIPLY.name());
        salesProcessor.process(adjustAppleMessage2);

        //Process 30 Sale messages on Orange
        for(int i=0; i < 18 ; i++)
        {
            Message message2 = new MessageType2(new Sale("Orange", 2.0), 5);
            salesProcessor.process(message2);
        }

        //Process Adjustment for Orange
        Message adjustOrangeMessage = new MessageType3(new Sale("Orange", 2.0), SaleAdjustmentOperation.MULTIPLY.name());
        salesProcessor.process(adjustOrangeMessage);



        assertEquals(ErrorMessage.MESSAGE_COUNT_MISMATCH.toString(),50, salesProcessor.getNumberOfMessagesProcessed());
        String outputMessage = salesProcessor.checkAndGetAdjustmentReportAfterFiftyMessages();
        assertEquals(ErrorMessage.LOG_MESSAGE_MISMATCH.toString(), expectedMessage, outputMessage);

    }


    private class SalesProcessorForTest extends SalesProcessor
    {

    }

    private enum ErrorMessage
    {
        PRICE_MISMATCH("Price is Not As Expected"),
        MESSAGE_COUNT_MISMATCH("Message Count is not as Expected"),
        ADJUSTMENT_COUNT_MISMATCH("Adjustment Count is not as Expected"),
        SALES_MISMATCH("Number of Sales is not as expected"),
        LOG_MESSAGE_MISMATCH("Log Message is Nor Correct");

        String errorMessage;

        ErrorMessage(String errorMessage)
        {
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString()
        {
            return this.errorMessage;
        }
    }

}

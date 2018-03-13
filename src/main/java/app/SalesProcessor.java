package app;

import domain.Message;
import domain.MessageType3;
import domain.Sale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesProcessor
{
   private int numberOfMessagesProcessed = 0;
   private Map<String, List<Sale>> productTypeToProcessedSales = new HashMap<>();
   private Map<String, List<MessageType3>> adjustmentMessages = new HashMap<>();
   private boolean isPaused = false;
   private static final int ADJUSTMENT_LOG_THRESHOLD = 50;
   private static final Logger logger = LogManager.getLogger(SalesProcessor.class);

   public void process(Message message)
   {
       if(!isPaused)
       {
           processMessageAndIncrementCount(message);
           checkAndPrintIfNotEmpty(checkAndGetSalesReportForEveryTenthMessage());
           checkAndPrintIfNotEmpty(checkAndGetAdjustmentReportAfterFiftyMessages());
       }
       else
       {
           logger.info("No messages being processed, rejecting message {}", message.toString());
       }
   }

    private void processMessageAndIncrementCount(Message message)
    {
        if(message.hasAdjustment())
        {
            processAdjustment((MessageType3) message);
        }
        else
        {
            processSale(message);
        }
        incrementMessagesProcessed();
    }

    private void processSale(Message message)
    {
        String productType = message.getSale().getProductType();
        double price = message.getSale().getPrice();

        logger.debug("Processing sale for product type {} and price {}",
                productType,
                price);

        for(int i=0; i < message.numberOfSales(); i++)
        {
            Sale newSale = new Sale(productType, price);
            this.productTypeToProcessedSales.computeIfAbsent(productType, k -> new ArrayList<>()).add(newSale);
        }
    }

    private void processAdjustment(MessageType3 message)
    {
        String productType = message.getSale().getProductType();

        logger.debug("Processing adjustment for product type {} and price {}",
                productType,
                message.getSale().getPrice());


        List<Sale> processedSales = this.productTypeToProcessedSales.get(productType);
        for(Sale sale : processedSales)
        {
            sale.applyAdjustment(message.getSale(), message.getSaleAdjustmentOperation());
        }

        this.adjustmentMessages.computeIfAbsent(productType, k -> new ArrayList<>()).add(message);
    }

   protected String checkAndGetSalesReportForEveryTenthMessage()
   {
       StringBuilder salesReport = new StringBuilder();
       if(numberOfMessagesProcessed % 10 == 0)
       {
           logger.debug("Processed tenth message, will print all sales till now");

           this.productTypeToProcessedSales.forEach((productType, sales) ->
                  salesReport.append(String.format("For productType %s there are %d sales made for total value of %f\n",
                          productType,
                          sales.size(),
                          sales.stream().mapToDouble(Sale::getPrice).sum()))

          );
       }
       return salesReport.toString();
   }

    private void checkAndPrintIfNotEmpty(String messageLog)
    {
        if(!messageLog.isEmpty())
        {
            System.out.println(messageLog);
        }
    }


   protected String checkAndGetAdjustmentReportAfterFiftyMessages()
   {
       StringBuilder adjustmentReport = new StringBuilder();
       if(numberOfMessagesProcessed == ADJUSTMENT_LOG_THRESHOLD)
       {
           logger.debug("Processed fifty messages, will pause and print all adjustments");

           this.isPaused = true;
           this.adjustmentMessages.forEach(
                   (productType, messages) ->
                   {
                       adjustmentReport.append(String.format("For productType %s adjustments made are:\n", productType));
                       messages.forEach(messageType3 -> adjustmentReport.append(messageType3.toString() + "\n"));
                   });
       }
       return adjustmentReport.toString();
   }

   private void incrementMessagesProcessed()
   {
       this.numberOfMessagesProcessed++;
   }

    public int getNumberOfMessagesProcessed()
    {
        return numberOfMessagesProcessed;
    }

    public Map<String, List<Sale>> getProductTypeToProcessedSales()
    {
        return productTypeToProcessedSales;
    }

    public Map<String, List<MessageType3>> getAdjustmentMessages()
    {
        return adjustmentMessages;
    }

    public boolean isPaused()
    {
        return isPaused;
    }

}

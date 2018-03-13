# salesprocessor
Sales Proceccor to process different messages with sales. Processor can process three types of messages. 

Message Type 1 - Message for unit sale of a product 
Message Type 2 - Message for multiple sales of a product 
Message Type 3 - Message to adjust prior sales. Adjustments can be to add, subtract or multiply sale price. 



Prerequisites

JDK 8



Code Structure

Main class where most of the sale processing logic residdes is - "SalesProcessor".
Possible adjustment operations are defined under class - "SaleAdjustmentOperation"



Running the tests(Junit4)

  Unit test - "SalesProcessorTest" tests the logc in "SalesProcessor" class
  
  Unit test - "SaleAdjustmentOperationTest" tests behavior of different types of adjustments
  
  Unit test - "SaleTest" tests the behavioe when a adjustment is applied on a "Sale" object



Built With

Maven - Dependency Management

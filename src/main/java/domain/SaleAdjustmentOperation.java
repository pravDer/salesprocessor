package domain;

import java.util.HashSet;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;

public enum SaleAdjustmentOperation
{
  ADD("ADD", (a, b) -> a + b),
  SUB("SUB", (a, b) -> a - b),
  MULTIPLY("MULTIPLY", (a, b) -> a * b);


  String adjustmentName;
  private DoubleBinaryOperator adjustmentOperator;

  SaleAdjustmentOperation(String adjustmentName,
                          DoubleBinaryOperator adjustmentOperator)
  {
    this.adjustmentName = adjustmentName;
    this.adjustmentOperator = adjustmentOperator;
  }

  public double apply(double a, double b)
  {
    return adjustmentOperator.applyAsDouble(a, b);
  }

  public static Set<String> getValidAdjustmentOperations()
  {
    Set<String> validOperations = new HashSet<>();
    for(SaleAdjustmentOperation validOperation : SaleAdjustmentOperation.values())
    {
      validOperations.add(validOperation.adjustmentName);
    }
    return validOperations;
  }
}

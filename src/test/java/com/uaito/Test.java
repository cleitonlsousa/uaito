package com.uaito;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Test {

	@org.junit.Test
	public void contextLoads() {

		BigDecimal accruedValue = BigDecimal.valueOf(0.0158);

		BigDecimal temp = accruedValue.setScale(2, RoundingMode.DOWN);

		System.out.println(accruedValue);

		System.out.println(temp);

		System.out.println(accruedValue.subtract(temp));


	}

}

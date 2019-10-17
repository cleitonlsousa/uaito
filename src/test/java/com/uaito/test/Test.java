package com.uaito.test;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Test {

	@org.junit.Test
	public void test() {

		BigDecimal accruedValue = BigDecimal.valueOf(0.0158);

		BigDecimal temp = accruedValue.setScale(2, RoundingMode.DOWN);

		System.out.println(accruedValue);

		System.out.println(temp);

		System.out.println(accruedValue.subtract(temp));


	}

}

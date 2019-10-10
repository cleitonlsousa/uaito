package com.uaito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UaitoApplicationTests {

	@Test
	public void contextLoads() {

		BigDecimal accruedValue = BigDecimal.valueOf(0.0158);

		System.out.println(accruedValue.setScale(2, RoundingMode.HALF_DOWN));
	}

}

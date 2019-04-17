package testValidator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import validators.CurrencyValidator;

public class CurrencyValidatorTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsCurrencyFormatValidInputShouldNotBeValid() {
		assertFalse(CurrencyValidator.isCurrencyFormatValid("ksdlaksjd"));
		assertFalse(CurrencyValidator.isCurrencyFormatValid(""));
		assertFalse(CurrencyValidator.isCurrencyFormatValid("---****"));
	}
	
	@Test
	public void testIsCurrencyFormatValidInputShouldBeValid() {
		assertTrue(CurrencyValidator.isCurrencyFormatValid("AUD"));
		assertTrue(CurrencyValidator.isCurrencyFormatValid("usd"));
		assertTrue(CurrencyValidator.isCurrencyFormatValid("caD"));
	}

}

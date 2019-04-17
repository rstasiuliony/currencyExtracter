package testValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import validators.DateValidator;

public class DateValidatorTest {
	
	private static final String TODAY = LocalDate.now().toString();
	private static final String YESTERDAY = LocalDate.now().minusDays(1).toString();
	private static final String TOMORROW = LocalDate.now().plusDays(1).toString();
	private static final String FOURTYYEARSAGO = LocalDate.now().minusYears(40).toString();
	private static final String SIXTYYEARSAGO = LocalDate.now().minusYears(60).toString();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsDateFormatValidShouldNotBeValid() {
		List<String> test = new ArrayList<>(Arrays.asList("12dsas231", "1982/01/01", "82/01/01", "1982.12.12", "2010-20-30", "2010-00-00"));
		test.stream().forEach(e -> { 
			if(DateValidator.isDateFormatValid(e)){
				fail("Test failed with element " + e);
			}
		});
	}

	@Test
	public void testIsDateFormatValidShouldBeValid() {
		assertTrue(DateValidator.isDateFormatValid(TODAY));
		assertTrue(DateValidator.isDateFormatValid(YESTERDAY));
	}
	
	@Test
	public void testIsStartDateBeforeEndDateShouldNotBeValid() {
		assertFalse(DateValidator.isStartDateBeforeEndDate(TOMORROW, YESTERDAY));
		assertFalse(DateValidator.isStartDateBeforeEndDate(TODAY, FOURTYYEARSAGO));	
	}
	
	@Test
	public void testIsStartDateBeforeEndDateShouldBeValid() {
		assertTrue(DateValidator.isStartDateBeforeEndDate(YESTERDAY, TOMORROW));
		assertTrue(DateValidator.isStartDateBeforeEndDate(FOURTYYEARSAGO, TODAY));
	}
	
	@Test
	public void testIsDateInLitasShouldNotBeValid() {
		assertFalse(DateValidator.isDateInLitas(FOURTYYEARSAGO));
		assertFalse(DateValidator.isDateInLitas(SIXTYYEARSAGO));	
	}
	
	@Test
	public void testIsDateInLitasShouldBeValid() {
		assertTrue(DateValidator.isDateInLitas(TODAY));
		assertTrue(DateValidator.isDateFormatValid(SIXTYYEARSAGO));
	}
}

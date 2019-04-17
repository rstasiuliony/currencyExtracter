package validators;

import main.CurrencyFetcher;

public final class CurrencyValidator {
	
	private CurrencyValidator() {}
	
	public static boolean isCurrencyFormatValid(String currencyCode) {
		CurrencyFetcher cf = new CurrencyFetcher();
		return cf.getCurrencyList().stream().filter(c -> c.getCurrencyCode().equalsIgnoreCase(currencyCode)).findFirst().isPresent() ? true : false;
	}

}

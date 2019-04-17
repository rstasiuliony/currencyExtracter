package main;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import saxHandlers.ListByDateAndCodeHandler;
import saxHandlers.ListOfCurrencyHandler;

public class CurrencyFetcher {

	private SAXParserFactory factory;

	public CurrencyFetcher() {
		factory = SAXParserFactory.newInstance();
	}

	public List<Currency> getCurrencyList() {
		ListOfCurrencyHandler handler = new ListOfCurrencyHandler();
		String currencyListURL = "http://old.lb.lt/webservices/fxrates/FxRates.asmx/getCurrencyList?";
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(currencyListURL, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return handler.getCurrency();
	}

	public List<Currency> getCurrencyByDateAndCurrencyCode(String startDate, String endDate, String currencyCode) {
		ListByDateAndCodeHandler handler = new ListByDateAndCodeHandler();
		if(currencyCode==null) {
			currencyCode = "";
		}
		String currencyListByDateAndCodeURL = "http://old.lb.lt/webservices/fxrates/FxRates.asmx/getFxRatesForCurrency?tp=lt&ccy="
				.concat(currencyCode).concat("&dtFrom=".concat(startDate).concat("&dtTo=").concat(endDate));
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(currencyListByDateAndCodeURL, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getCurrencyByDateAndCode(startDate, endDate, currencyCode);
	}

}

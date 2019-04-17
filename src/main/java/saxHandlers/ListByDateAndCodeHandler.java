package saxHandlers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import main.Currency;

public class ListByDateAndCodeHandler extends DefaultHandler {
	
	private List<Currency> currencyList = new ArrayList<>();
	private Currency currency;
	private boolean date = false;
	private boolean currencyCode = false;
	private boolean amount = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
			case "FxRate": {
				currency = new Currency();
				break;
			}
			case "Dt": {
				date = true;
				break;
			}
			case "Ccy": {
				currencyCode = true;
				break;
			}
			case "Amt": {
				amount = true;
				break;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "FxRate": {
				currencyList.add(currency);
				break;
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (date) {
			currency.setDate(new String(ch, start, length));
			date = false;
		} else if (currencyCode) {
			currency.setCurrencyCode(new String(ch, start, length));
			currencyCode = false;
		} else if (amount) {
			currency.setAmount(new String(ch, start, length));
			amount = false;
		}
	}
	
	public List<Currency> getCurrencyByDateAndCode(String startDate, String endDate, String currencyCode2) {
		return currencyList;
	}
}

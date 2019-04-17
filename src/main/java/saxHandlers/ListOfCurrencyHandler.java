package saxHandlers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import main.Currency;

public class ListOfCurrencyHandler extends DefaultHandler {

	private List<Currency> currencyList = new ArrayList<>();
	private Currency currency;
	private boolean currencyCode = false;
	private boolean currencyName = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
			case "CcyNtry": {
				currency = new Currency();
				break;
			}
			case "Ccy": {
				currencyCode = true;
				break;
			}
			case "CcyNm": {
				if (attributes.getValue("lang") != null && attributes.getValue("lang").equalsIgnoreCase("LT")) {
					currencyName = true;
				}
				break;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "CcyNtry": {
				currencyList.add(currency);
				break;
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (currencyCode) {
			currency.setCurrencyCode(new String(ch, start, length));

			currencyCode = false;
		} else if (currencyName) {
			currency.setCurrencyName(new String(ch, start, length));
			currencyName = false;
		}
	}

	public List<Currency> getCurrency() {
		return currencyList;
	}
}

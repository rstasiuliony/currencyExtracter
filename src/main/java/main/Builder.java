package main;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import validators.CurrencyValidator;
import validators.DateValidator;

public class Builder {

	private Scanner readInstructions;
	private CurrencyFetcher currencyFetcher;
	private List<Currency> list;
	private String startDate;
	private String endDate;
	private String currencyCode;

	public Builder() {
		readInstructions = new Scanner(System.in);
		currencyFetcher = new CurrencyFetcher();
		initiateProgram();
	}

	private void initiateProgram() {
		System.out.println("Pasirinkite norim� operacij�, �vesdami jos numer�:\n"
				+ "1 - pateikti valiut� keitimo kursus pagal datas ir (ar) valiutos kod�\n"
				+ "2 - pateikti naujausi� valiutos keitimo kurs� pagal valiutos kod�\n" 
				+ "3 - u�daryti program�");
		read(readInstructions.nextLine());
	}

	private void read(String value) {
		switch (value) {
		case "1":
			list = initiateCurrencyByDateAndCode();
			printCurrencyByDateAndCode();
			if (endDate != null && !currencyCode.isEmpty()) {
				printChangeByPeriod();
			}
			break;
		case "2":
			currencyCode = insertCurrencyCode(true);
			list = currencyFetcher.getCurrencyByDateAndCurrencyCode(LocalDate.now().toString(), LocalDate.now().toString(), currencyCode);
			printCurrencyByDateAndCode();
			break;
		case "3":
			close();
			break;
		default:
			System.out.println("Turite �vesti skai�i� nuo 1 iki 3. Bandykite dar kart�.\n");
			initiateProgram();
		}
	}

	private List<Currency> initiateCurrencyByDateAndCode() {
		startDate = insertDate("prad�ios");
		endDate = insertDate("pabaigos");
		if (!DateValidator.isStartDateBeforeEndDate(startDate, endDate)) {
			initiateCurrencyByDateAndCode();
		};
		String cCode = insertCurrencyCode(false);
		return currencyFetcher.getCurrencyByDateAndCurrencyCode(startDate, endDate, cCode);
	}

	private String insertDate(String value) {
		boolean dateFormatValid = false;
		boolean dateAfterLitas = false;
		String date = null;
		while (!dateFormatValid || !dateAfterLitas) {
			System.out.println("�veskite periodo " + value + " dat� (formatas: MMMM-MM-DD).");
			date = readInstructions.nextLine();
			dateFormatValid = DateValidator.isDateFormatValid(date);
			dateAfterLitas = DateValidator.isDateInLitas(date);
		}
		return date;
	}

	private String insertCurrencyCode(boolean codeMandatory) {
		System.out.println("�veskite valiutos kod�. Nor�dami matyti visas valiutas, spauskite ENTER.");
		currencyCode = readInstructions.nextLine();
		if (!currencyCode.isEmpty() || codeMandatory) {
			while (!CurrencyValidator.isCurrencyFormatValid(currencyCode)) {
				System.out.println("�vestas kodas neteisingas. Pateikiame galim� kod� s�ra��:\n");
				printListOfCurrencies();
				System.out.println("\n�veskite valiutos kod�:");
				currencyCode = readInstructions.nextLine().toUpperCase();
			}
			return currencyCode;
		} 
		return null;
	}

	private void printListOfCurrencies() {	
		 currencyFetcher.getCurrencyList().stream()
				.sorted((o1, o2) -> o1.getCurrencyName().compareTo(o2.getCurrencyName()))
				.forEach(o -> System.out.println(o.getCurrencyName() + " - " + o.getCurrencyCode()));
	}

	private void printCurrencyByDateAndCode() {
		if (list.size()==0) {
			System.out.println("�ios valiutos duomen� �iuo metu neturime.");
		} else {
			list.stream().sorted(Comparator.comparing(Currency::getDate).thenComparing(Currency::getCurrencyCode))
				.forEach(o -> {
					System.out.print(o.getDate() + ": EUR - 1 / " + o.getCurrencyCode() + " - " + o.getAmount() + "\n");
				});
		}
	}

	private void printChangeByPeriod() {
		if (!currencyCode.isEmpty()) {
			double roundedChangeResult = (double) Math.round(calculateChangeByPeriod() * 100000d) / 100000d;
			System.out.println(currencyCode + " valiutos pokytis nuo periodo prad�ios: " + roundedChangeResult);
		}
	}

	private double calculateChangeByPeriod() {
		String amountStartPeriod = "";
		String amountEndPeriod = "";
		
		for (Currency el : list) {
			if (startDate.equals(el.getDate())) {
				amountStartPeriod = el.getAmount();
			} else if (endDate.equals(el.getDate())) {
				amountEndPeriod = el.getAmount();
			}
		} 
		return Double.parseDouble(amountEndPeriod) - Double.parseDouble(amountStartPeriod);
	}

	private void close() {
		System.out.println("D�kojame, kad naudojat�s m�s� paslaugomis. Geros dienos!");
		readInstructions.close();
	}
}

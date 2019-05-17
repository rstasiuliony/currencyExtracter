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
		System.out.println("Pasirinkite norimà operacijà, ávesdami jos numerá:\n"
				+ "1 - pateikti valiutø keitimo kursus pagal datas ir (ar) valiutos kodà\n"
				+ "2 - pateikti naujausià valiutos keitimo kursà pagal valiutos kodà\n" 
				+ "3 - uþdaryti programà");
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
			System.out.println("Turite ávesti skaièiø nuo 1 iki 3. Bandykite dar kartà.\n");
			initiateProgram();
		}
	}

	private List<Currency> initiateCurrencyByDateAndCode() {
		startDate = insertDate("pradþios");
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
			System.out.println("Áveskite periodo " + value + " datà (formatas: MMMM-MM-DD).");
			date = readInstructions.nextLine();
			dateFormatValid = DateValidator.isDateFormatValid(date);
			dateAfterLitas = DateValidator.isDateInLitas(date);
		}
		return date;
	}

	private String insertCurrencyCode(boolean codeMandatory) {
		System.out.println("Áveskite valiutos kodà. Norëdami matyti visas valiutas, spauskite ENTER.");
		currencyCode = readInstructions.nextLine();
		if (!currencyCode.isEmpty() || codeMandatory) {
			while (!CurrencyValidator.isCurrencyFormatValid(currencyCode)) {
				System.out.println("Ávestas kodas neteisingas. Pateikiame galimø kodø sàraðà:\n");
				printListOfCurrencies();
				System.out.println("\nÁveskite valiutos kodà:");
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
			System.out.println("Ðios valiutos duomenø ðiuo metu neturime.");
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
			System.out.println(currencyCode + " valiutos pokytis nuo periodo pradþios: " + roundedChangeResult);
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
		System.out.println("Dëkojame, kad naudojatës mûsø paslaugomis. Geros dienos!");
		readInstructions.close();
	}
}

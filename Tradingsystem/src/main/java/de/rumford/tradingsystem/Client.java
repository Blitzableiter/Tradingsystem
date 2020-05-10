package de.rumford.tradingsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.rumford.tradingsystem.helper.CsvFormat;
import de.rumford.tradingsystem.helper.DataSource;
import de.rumford.tradingsystem.helper.ValueDateTupel;

public class Client {

	private Client() {
	}

	static boolean tryAgainFlag = true;
	/* Setup */
	static final String WORKING_DIR = Path.of("src", "test", "resources").toString();
	static final BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));
	static BaseValue baseValue;
	static Registrar registrar;

	public static void main(String[] args) throws IOException {

		baseValueSetup();

//		ruleRegistration();

		ruleSetup();

		System.out.println("Goodbye!");
	}

	private static void ruleRegistration() throws IOException {
		registrar = new Registrar();
		boolean addRulesFlag;
		tryAgainFlag = true;

		while (tryAgainFlag) {
			try {
				tryAgainFlag = false;
				String input;

				/* Read in class names */
				addRulesFlag = ynQuestion("Do you want to register additional types of rules?", "N");

				if (addRulesFlag) {
					tryAgainFlag = true;
					input = askForInput("Which rule do you want to register?");
					@SuppressWarnings("unchecked")
					Class<? extends Rule> clazz = (Class<? extends Rule>) Class.forName(input);
					registrar.registerRule(clazz);
					System.out.println("Rule successfully registered.");
				}
			} catch (ClassNotFoundException e) {
				System.out.println("The class " + e.getMessage()
						+ " could not be found. Please provide the fully qualified name.");
				tryAgainFlag = tryAgainQuestion();
			} catch (Exception e) {
				System.out.println("That didn't work. An error was thrown: " + e.getMessage());
				tryAgainFlag = tryAgainQuestion();
			}
		}
	}

	private static boolean tryAgainQuestion() throws IOException {
		return ynQuestion("Do you want to try again?", "N");
	}

	private static void ruleSetup() throws IOException {
		tryAgainFlag = true;
		Rule[] rules = {};

		while (tryAgainFlag) {
			try {
				String input;

				/* Read in file names */
				input = askForInput("Which rule do you want to use?");
				@SuppressWarnings("unchecked")
				Class<? extends Rule> clazz = (Class<? extends Rule>) Class.forName(input);

				tryAgainFlag = false;
			} catch (ClassNotFoundException e) {
				System.out.println("The class " + e.getMessage()
						+ " could not be found. Please provide the fully qualified name.");
				tryAgainFlag = tryAgainQuestion();
			} catch (Exception e) {
				System.out.println("That didn't work. An error was thrown: " + e.getMessage());
				tryAgainFlag = tryAgainQuestion();
			}
		}
	}

	private static void baseValueSetup() throws IOException {
		String nameInput = askForInput("What name should this base value have?");

		while (tryAgainFlag) {
			try {
				String input;

				/* Read in file names */
				input = askForInput("Which data source do you want to use?");
				ValueDateTupel[] values = readValuesFromCsv(Path.of(WORKING_DIR, input).toString(), CsvFormat.EU);

				if (ynQuestion("Do you want to provide short index values?", "N")) {
					input = askForInput("Which data source do you want to use?");
					ValueDateTupel[] shortIndexValues = readValuesFromCsv(Path.of(WORKING_DIR, input).toString(),
							CsvFormat.EU);

					ValueDateTupel[][] notAligned = { values, shortIndexValues };
					ValueDateTupel[][] aligned = ValueDateTupel.alignDates(notAligned);

					/* Construct BaseValue */
					baseValue = new BaseValue(nameInput, aligned[0], aligned[1]);
				} else {
					baseValue = new BaseValue(nameInput, values);
				}

				System.out.println("Data sources succesfully read.");

				tryAgainFlag = false;
			} catch (Exception e) {
				System.out.println("That didn't work. An error was thrown: " + e.getMessage());
				tryAgainFlag = tryAgainQuestion();
			}
		}
		tryAgainFlag = true;
	}

	private static String askForInput(String question) throws IOException {
		System.out.print(question + " ");
		return IN.readLine();
	}

	private static boolean ynQuestion(String question, String defaultValue) throws IOException {
		StringBuilder questionSB = new StringBuilder();
		questionSB.append(question);
		questionSB.append(" ");
		String options = (defaultValue.toLowerCase().contentEquals("y") ? "Y/n" : "y/N");
		questionSB.append(options);
		questionSB.append(" > ");

		boolean unRecognizedFlag = false;
		while (!unRecognizedFlag) {
			System.out.println(questionSB.toString());
			String input = IN.readLine();
			if (input.toLowerCase().contentEquals("y")) {
				return true;
			} else if (input.contentEquals("") || input.toLowerCase().contentEquals("n")) {
				return false;
			} else {
				System.out.println("Option not recognized. Try again.");
				unRecognizedFlag = true;
			}
		}
		return false;

	}

	private static ValueDateTupel[] readValuesFromCsv(String path, CsvFormat csvFormat) throws IOException {
		return DataSource.getDataFromCsv(path, csvFormat);
	}

	static class Registrar {

		List<Class<? extends Rule>> registeredRules;

		public Registrar() {
			registeredRules = new ArrayList<>();
			registeredRules.add(VolatilityDifference.class);
			registeredRules.add(EWMAC.class);
		}

		public void registerRule(Class<? extends Rule> ruleClass) {
			List<Class<? extends Rule>> currentRules = this.getRegisteredRules();
			currentRules.add(ruleClass);
			this.setRegisteredRules(currentRules);
		}

		public List<Class<? extends Rule>> getRegisteredRules() {
			return this.registeredRules;
		}

		private void setRegisteredRules(List<Class<? extends Rule>> registeredRules) {
			this.registeredRules = registeredRules;
		}
	}
}

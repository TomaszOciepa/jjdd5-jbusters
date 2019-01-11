package com.infoshareacademy.jbusters.console;

import com.infoshareacademy.jbusters.data.Data;
import com.infoshareacademy.jbusters.data.PropLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Properties;

public class MenuOptions {
    private static final URL APP_PROPERTIES_FILE = Thread.currentThread().getContextClassLoader().getResource("app.properties");
    private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);

    private ConsoleReader consoleReader = new ConsoleReader();
    private int decimalPlaces;
    private String currency;
    private BigDecimal exchangeRate;
    private BigDecimal areaDiff;
    private BigDecimal areaDiffExpanded;
    private int minResults;
    private BigDecimal priceDiff;

    void loadOptionsMenu() throws IOException {

        PropLoader properties = new PropLoader();
        try {
            properties = new PropLoader(APP_PROPERTIES_FILE.openStream());
            decimalPlaces = properties.getDecimalPlaces();
            currency = properties.getCurrency();
            exchangeRate = new BigDecimal(properties.getExchangeRate());
            areaDiff = properties.getAreaDiff();
            areaDiffExpanded = properties.getAreaDiffExpanded();
            minResults = properties.getMinResultsNumber();
            priceDiff = properties.getPriceDiff();

        } catch (IOException e) {
            LOGGER.error("Missing properties file in path {}", APP_PROPERTIES_FILE.toString());
        }

        int menuChoice = 0;
        while (menuChoice != 2) {
            System.out.println("\n          O P C J E\n");
            System.out.println("Aktualna ilość miejsc dziesiętnych:\t\t\t\t" + decimalPlaces + "\n" +
                    "Aktualna waluta:\t\t\t\t\t\t" + currency + "\n" +
                    "Aktualny kurs:\t\t\t\t\t\t\t1 " + currency + " = " + exchangeRate + " PLN\n" +
                    "Aktualna podstawa zakresu filtrowania powierzchni:\t\t" + areaDiff + " m2\n" +
                    "Aktualne rozszerzenie zakresu filtrowania powierzchni:\t\t" + areaDiffExpanded + " m2\n" +
                    "Aktualna, minimalna ilość transakcji potrzebnych do wyceny:\t" + minResults + "\n" +
                    "Aktualny parametr maksymalnego odchylenia ceny skrajnej:\t" + priceDiff + " " + currency + " za m2\n\n" +
                    "1 - Zmiana powyższych ustawień i zapis" + "\n" +
                    "2 - Powrót do menu głównego" + "\n\n" +
                    "Podaj numer:");
            menuChoice = consoleReader.readInt(1, 2);
            optionsMenuSwitch(menuChoice);
        }
        System.exit(0);
    }


    private void optionsMenuSwitch(int Choice) throws IOException {
        switch (Choice) {
            case 1: {
                Properties properties = new Properties();
                FileWriter write = new FileWriter("app/app.properties");

                System.out.println("\n" + ":: Wybrano zmianę ustawień ::" + "\n");

                System.out.println("Podaj nową ilość miejsc dziesiętnych (cyfry od 0 do 2):");
                String decimalPlaces = consoleReader.readStringDecimalPlaces();

                properties.setProperty("decimalPlaces", decimalPlaces);

                System.out.println("Podaj nową walutę (3 duże litery):");
                String currency = consoleReader.readStringCurrency();
                properties.setProperty("currency", currency);

                System.out.println("Podaj nowy kurs ? PLN = 1 " + currency + " (format #.##):");
                String exchangeRate = consoleReader.readStringExchangeRate();
                properties.setProperty("exchangeRate", exchangeRate);

                System.out.println("Podaj nowy, podstawowy zakres filtrowania powierzchni (m2):");
                String areaDiff = consoleReader.readStringExchangeRate();
                properties.setProperty("areaDiff", areaDiff);

                System.out.println("Podaj nowy, rozszerzony zakres filtrowania powierzchni (m2):");
                String areaDiffExpanded = consoleReader.readStringExchangeRate();
                properties.setProperty("areaDiffExpanded", areaDiffExpanded);

                System.out.println("Podaj nową, minimalną ilość transakcji potrzebnych do wyceny:");
                String minResults = consoleReader.readStringExchangeRate();
                properties.setProperty("minResults", minResults);

                System.out.println("Podaj nowy parametr maksymalnego odchylenia ceny skrajnej (" + currency + " za m2):");
                String priceDiff = consoleReader.readStringExchangeRate();
                properties.setProperty("priceDiff", priceDiff);

                properties.store(write, "Properties File");

                System.out.println("\n:: Nowe parametry zostały zapisane ::\n");

                MenuOptions viewChanges = new MenuOptions();
                viewChanges.loadOptionsMenu();

                break;
            }
            case 2: {
                Menu menu = new Menu();
                ConsoleViewer.clearScreen();
                menu.loadMenu();
                break;
            }
        }
    }

}

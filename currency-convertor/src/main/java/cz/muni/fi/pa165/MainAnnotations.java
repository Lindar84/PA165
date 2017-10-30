package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Creates Spring ApplicationContext based on JSR-330 annotations.
 *
 * @author Ludmila Fialova
 */
public class MainAnnotations {
    public static void main(String ... args) {

        ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext("cz.muni.fi.pa165");

        CurrencyConvertor currencyConvertor
                = applicationContext.getBean(CurrencyConvertor.class);

        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        // get instance of CurrencyConvertor and try convert one euro to czk. Test, if the main method is working well.
        System.err.println(currencyConvertor.convert(eur, czk, new BigDecimal(2)));
    }
}

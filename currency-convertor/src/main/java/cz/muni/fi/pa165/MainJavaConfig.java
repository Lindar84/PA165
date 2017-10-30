package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.SpringJavaConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * creates Spring ApplicationContext based on this JavaConfig application context configuration.
 *
 * @author Ludmila Fialova
 */
public class MainJavaConfig {
    public static void main(String ... args) {

        ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(SpringJavaConfig.class);

        CurrencyConvertor currencyConvertor
                = applicationContext.getBean("currencyConvertor", CurrencyConvertor.class);

        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        // Then get instance of CurrencyConvertor and try convert one euro to czk. Test, if the main method is working well.
        System.err.println(currencyConvertor.convert(eur, czk, new BigDecimal(3)));
    }
}

package cz.muni.fi.pa165;


import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * method creates Spring ApplicationContext based on Spring xml application context configuration.
 *
 * @author Ludmila Fialova
 */
public class MainXml {

    public static void main(String ... args) {

        ApplicationContext applicationContext
                = new ClassPathXmlApplicationContext("springApplicationContext.xml");

        CurrencyConvertor currencyConvertor
                = applicationContext.getBean(CurrencyConvertor.class);

        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        // get instance of CurrencyConvertor and try convert one euro to czk. Test, if the main method is working well.
        System.err.println(currencyConvertor.convert(eur, czk, new BigDecimal(1)));
    }

}

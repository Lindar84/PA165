package cz.muni.fi.pa165.currency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.inject.Inject;

/**
 * configures components ExchangeRateTableImpl and CurrencyConvertorImpl.
 *
 * @author Ludmila Fialova
 */
@Configuration
@ComponentScan("cz.muni.fi.pa165.currency")
@EnableAspectJAutoProxy     // to enable automatic aspectj proxy creation
public class SpringJavaConfig {

    @Inject
    private ExchangeRateTable exchangeRateTable;

    @Bean
    public CurrencyConvertor currencyConvertor() {
        System.err.println("Creating currencyConvertor");
        return new CurrencyConvertorImpl(exchangeRateTable);
    }

}

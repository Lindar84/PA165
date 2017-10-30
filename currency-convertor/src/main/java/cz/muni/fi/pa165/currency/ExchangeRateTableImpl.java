package cz.muni.fi.pa165.currency;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * TODO: create  javadoc
 *
 * @author Ludmila Fialova
 */
@Named
public class ExchangeRateTableImpl implements ExchangeRateTable  {

    private final BigDecimal exchangeRate = new BigDecimal(27);

    @Override
    public BigDecimal getExchangeRate(Currency sourceCurrency, Currency targetCurrency) throws ExternalServiceFailureException {

        if (sourceCurrency == null) {
            throw new IllegalArgumentException("sourceCurrency is null");
        }
        if (targetCurrency == null) {
            throw new IllegalArgumentException("targetCurrency is null");
        }
        if(sourceCurrency.toString().equals("EUR")){
            if(targetCurrency.toString().equals("CZK")){
                return exchangeRate;
            }
        }
        return null;
    }
}

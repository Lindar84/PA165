package cz.fi.muni.pa165.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring
 * configure the Jackson Object Mapper by using  setDateFormat
 * 1 - disable SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
 * 2 - use setDateFormat either using SimpleDateFormat or com.fasterxml.jackson.databind.util.ISO8601DateFormat;
 * TODO: 3 - You can rebuild the application and test that dates are now in a more human-readable format.
 *
 * @author Ludmila Fialova
 */
public class JacksonObjectMapper {

    private String date;

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter(Date date) {   // (SimpleDateFormat format)
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);   // ad 1
        objectMapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));  // ad 2
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}

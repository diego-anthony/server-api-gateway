package essalud.gob.pe.serverapigateway.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HttpMessageConvertersConfiguration {

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();

        //convertidores de mensajes predeterminados
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(new AllEncompassingFormHttpMessageConverter());

        return new HttpMessageConverters(converters);
    }
}
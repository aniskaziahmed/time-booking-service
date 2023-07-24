package com.fantasy.tbs.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestClientHelper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientHelper.class);

    private final RestTemplate restClient;
    private final ObjectMapper mapper;

    public RestClientHelper()
    {
        restClient = new RestTemplate();
        List<HttpMessageConverter<?>> converters = restClient.getMessageConverters();
        converters.removeIf(StringHttpMessageConverter.class::isInstance);
        converters.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        mapper = new ObjectMapper();
    }

    public <T> T getForEntity(String url, Class<T> classType, Object... param) throws RestClientException
    {
        ResponseEntity<String> response = restClient.getForEntity(url, String.class, param);

        JavaType javaType = mapper.getTypeFactory().constructType(classType);
        Optional<T> returnValue = mapResponseToClass(response, javaType);
        return returnValue.orElse(null);
    }

    private <T> Optional<T> mapResponseToClass(ResponseEntity<String> response, JavaType toType)
    {
        T returnValue = null;

        if (isSuccessfulResponse(response))
        {
            String body = response.getBody();
            if (StringUtils.isEmpty(body) || body.equals("[]") || body.equals("[{}]") || body.equals("{}"))
            {
                return Optional.empty();
            }

            try
            {
                returnValue = mapper.readValue(response.getBody(), toType);
            }
            catch (IOException e)
            {
                LOGGER.error("Couldn't convert rest api response into class type {}", toType, e);
            }
        }
        else
        {
            writeDetailLogs(response);
        }

        return Optional.of(returnValue);
    }

    private void writeDetailLogs(ResponseEntity<String> response)
    {
        LOGGER.error("Response HTTP Status: {}", response.getStatusCode());
        LOGGER.error("Response Body: {}", response.getBody());
    }

    private boolean isSuccessfulResponse(ResponseEntity<String> response)
    {
        return response.getStatusCode().is2xxSuccessful();
    }
}

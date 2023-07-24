package com.fantasy.tbs.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fantasy.tbs.domain.EmployeeDTO;
import com.fantasy.tbs.exception.EmployeeException;

//TODO Write unit tests for employee service
@Service
public class EmployeeService
{
    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private static final int MAX_RETRY = 3;
    @Value("${employee.service.api.domain}")
    private String employeeApiUrl;
    private static final String PATH = "/api/employees/";

    private final RestClientHelper restClientHelper;

    public EmployeeService(RestClientHelper restClientHelper)
    {
        this.restClientHelper = restClientHelper;
    }

    public List<EmployeeDTO> getAllEmployees() throws EmployeeException
    {
        return getAllEmployeesWithRetry(MAX_RETRY);
    }

    public List<EmployeeDTO> getAllEmployeesWithRetry(int retryCount) throws EmployeeException
    {
        EmployeeDTO[] employees;
        try
        {
            employees = restClientHelper.getForEntity(employeeApiUrl + PATH, EmployeeDTO[].class);
            if (Objects.isNull(employees))
            {
                throw new EmployeeException(
                    "No employees found, either something is wrong with employee service or maybe there is an issue with the mapper!!!");
            }
        }
        //It's possible that there might be any other exception be thrown here from getForEntity, so it's better to use Exception here for retrier
        catch (Exception e)
        {
            log.error("There is some problem with RestClientHelper.getForEntity method", e);
            if (retryCount > 0)
            {
                retryCount = retryCount - 1;
                try
                {
                    Thread.sleep((5 * 60 * 1000));

                    log.info("Retrying {}", retryCount);
                    return getAllEmployeesWithRetry(retryCount);
                }
                catch (InterruptedException ex)
                {
                    log.error("Thread.Sleep Exception: ", ex);
                }
            }

            throw new EmployeeException(e);
        }
        return Arrays.asList(employees);
    }
}

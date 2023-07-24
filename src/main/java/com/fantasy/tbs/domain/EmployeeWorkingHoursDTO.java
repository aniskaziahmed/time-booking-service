package com.fantasy.tbs.domain;

public class EmployeeWorkingHoursDTO
{
    private double totalWorkingHours;
    private EmployeeDTO employee;

    public EmployeeDTO getEmployee()
    {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee)
    {
        this.employee = employee;
    }

    public double getTotalWorkingHours()
    {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(double totalWorkingHours)
    {
        this.totalWorkingHours = totalWorkingHours;
    }
}

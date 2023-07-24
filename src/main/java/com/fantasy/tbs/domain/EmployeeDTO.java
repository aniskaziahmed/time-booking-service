package com.fantasy.tbs.domain;

//TODO Use lombok to avoid boilerplate code
public class EmployeeDTO
{
    private Long id;
    private String name;
    private String personalNumber;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPersonalNumber()
    {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber)
    {
        this.personalNumber = personalNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

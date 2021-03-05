package java_beans_entities;

import java.util.Set;

public class Administrator {
    /*
    :Clients הגישה למערכת מתחלקת לשלושה סוגי
    ניהול רשימת החברות ורשימת הלקוחות. – Administrator .1
    ניהול רשימת קופונים המשויכים לחברה. – Company .2
    רכישת קופונים. – Customer .3
     */
    private Set<Company> companySet;
    private Set<Customer> customerSet;

    public boolean add(Company company) {
        companySet.add(company);
        return true;//todo  maybe? Exception
    }
    public boolean add(Customer customer) {
        return customerSet.add(customer);// todo maybe? Exception
    }
}

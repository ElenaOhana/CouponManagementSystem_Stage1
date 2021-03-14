package facade;

import exceptions.CouponSystemException;

public class CustomerFacade extends ClientFacade {

    /*
     * From the facades, throw only CouponSystemException (or other custom exception dedicated to the project)
     * No SQL exception or anything like that
     *
     */
    private int customerId;

    public CustomerFacade(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        return false;
    }
}

package login;

import businesslogic.facade.*;
import exceptions.CouponSystemException;

public class LoginManager { // LoginManager is Singleton
    private static LoginManager instance;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
/**
* The method log in the client:
* the method checks by email and password param if client exists, and by clientType param which ClientFacade should return.
* returns an abstract client facade if the client exists(has the correct credentials) and has correct client type, otherwise throws CouponSystemException.
 * null is returned when the ClientType doesn't exists. */ //Factory method.
    public static ClientFacade login(String email, String password, ClientType clientType) throws CouponSystemException {
        if (clientType == ClientType.ADMINISTRATOR) {
            if (AdminFacade.login(email, password)) {
            ClientFacade adminFacade = new AdminFacade();
            return adminFacade;
            }
        } else if (clientType == ClientType.COMPANY) {
            if (CompanyFacade.login(email,password)) {
                int companyId = CompanyFacade.loginCompanyReturnId(email,password);
                CompanyFacade companyFacade = new CompanyFacade(companyId);
                return companyFacade;
            }
        } else if (clientType == ClientType.CUSTOMER) {
            if (CustomerFacade.login(email, password)) {
                int customerId = CustomerFacade.loginCustomerReturnId(email, password);
                ClientFacade customerFacade = new CustomerFacade(customerId);
                return customerFacade;
            }
        }
        return null;
    }
}

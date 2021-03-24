package login;

import businesslogic.facade.*;
import exceptions.CouponSystemException;

public class LoginManager { // LoginManager is Singleton
    private static LoginManager instance;

    private LoginManager() { //private Constructor
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public static ClientFacade login(String email, String password, ClientType clientType) throws CouponSystemException {
        if (clientType == ClientType.ADMINISTRATOR) {
            if (AdminFacade.login(email, password)) {
            ClientFacade adminFacade = new AdminFacade();
            return adminFacade;
            }
        } else if (clientType == ClientType.COMPANY) {
            if (CompanyFacade.login(email,password)) {
                int companyId = CompanyFacade.loginCompanyReturnId(email,password);
                ClientFacade companyFacade = new CompanyFacade(companyId);
                return  companyFacade;
            }
        } else if (clientType == ClientType.CUSTOMER) {
            if (CustomerFacade.login(email, password)) {
                int CustomerId = CustomerFacade.loginCustomerReturnId(email, password);
                ClientFacade customerFacade = new CustomerFacade(CustomerId);
                return customerFacade;
            }
        }
        return null;
    }
}

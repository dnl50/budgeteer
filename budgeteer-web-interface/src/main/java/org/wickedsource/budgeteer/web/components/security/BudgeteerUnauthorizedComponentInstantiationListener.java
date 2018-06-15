package org.wickedsource.budgeteer.web.components.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.web.BudgeteerApplication;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.BudgeteerSettings;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectWithKeycloakPage;

public class BudgeteerUnauthorizedComponentInstantiationListener implements IUnauthorizedComponentInstantiationListener {

    private boolean isKeycloakActivated;

    public BudgeteerUnauthorizedComponentInstantiationListener(boolean isKeycloakActivated){
        super();
        this.isKeycloakActivated = isKeycloakActivated;
    }

    @Override
    public void onUnauthorizedInstantiation(Component component) {
        if(BudgeteerSession.get().isLoggedIn()) {
            if(isKeycloakActivated) {
                component.setResponsePage(new SelectProjectWithKeycloakPage());
            }else{
                component.setResponsePage(new SelectProjectPage(LoginPage.class, new PageParameters()));
            }
        }else{
            component.setResponsePage(LoginPage.class);
        }
    }
}

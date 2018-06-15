package org.wickedsource.budgeteer.web.components.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.wickedsource.budgeteer.web.BudgeteerSession;

public class BudgeteerAuthorizationStrategy implements IAuthorizationStrategy {

    @Override
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
        NeedsLogin needsLogin = componentClass.getAnnotation(NeedsLogin.class);
        NeedsProject needsProject = componentClass.getAnnotation(NeedsProject.class);
        boolean isAnnotatedLogin = (needsLogin != null);
        boolean isAnnotatedProject = (needsProject != null);
        if(!isAnnotatedProject){
            if (!isAnnotatedLogin) {
                return true;
            } else {
                return BudgeteerSession.get().isLoggedIn();
            }
        }else{
            return BudgeteerSession.get().isLoggedIn() && BudgeteerSession.get().isInProject();
        }
    }

    @Override
    public boolean isActionAuthorized(Component component, Action action) {
        return true;
    }

    @Override
    public boolean isResourceAuthorized(IResource resource, PageParameters parameters) {
        return true;
    }
}

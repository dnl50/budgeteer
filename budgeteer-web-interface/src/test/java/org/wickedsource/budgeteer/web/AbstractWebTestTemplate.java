package org.wickedsource.budgeteer.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.web.pages.administration.Project;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public abstract class AbstractWebTestTemplate {

    @Autowired
    BudgeteerApplication application;

    @Autowired private ProjectService projectServiceMock;

    private static WicketTester tester;

    @BeforeEach
    public void setUp() {
        when(getProjectServiceMock().findProjectById(anyLong()))
                .thenReturn(new Project(0,new Date(), new Date(),"test"));
        if (tester == null) {
            tester = new WicketTester(application);
            login();
        }
        setupTest();
    }

    /**
     * Subclasses can use this method to provide the configuration needed by
     * each test.
     */
    protected abstract void setupTest();

    private void login() {
        User user = new User();
        user.setId(1L);
        user.setName("username");
        BudgeteerSession.get().login(user);
    }

    protected WicketTester getTester() {
        return tester;
    }

    protected ProjectService getProjectServiceMock() {
        return projectServiceMock;
    }
}

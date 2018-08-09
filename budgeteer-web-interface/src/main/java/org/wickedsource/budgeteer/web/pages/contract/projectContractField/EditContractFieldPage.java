package org.wickedsource.budgeteer.web.pages.contract.projectContractField;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.instantiation.NeedsProject;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.contract.projectContractField.fieldTable.ContractFieldTable;

@NeedsProject
@Mount("/contracts/fields")
public class EditContractFieldPage extends DialogPageWithBacklink {

    public EditContractFieldPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);

        this.setOutputMarkupId(true);
        this.addComponents();
    }

    private void addComponents() {
        add(new ContractFieldTable("contractFieldTable", BudgeteerSession.get().getProjectId()));
        add(createBacklink("backlink1"));
        add(createBacklink("backlink2"));
    }

}

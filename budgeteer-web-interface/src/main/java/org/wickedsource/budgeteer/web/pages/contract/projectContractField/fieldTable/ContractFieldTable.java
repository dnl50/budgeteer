package org.wickedsource.budgeteer.web.pages.contract.projectContractField.fieldTable;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;


public class ContractFieldTable extends WebMarkupContainer {

    private final long projectId;

    public ContractFieldTable(String id, long projectId) {
        super(id, new ContractFieldTableModel(projectId));

        this.projectId = projectId;

        this.addComponents();
    }

    public void addComponents() {
        add(new ContractFieldTableListView(""));
    }

    private class ContractFieldTableListView extends ListView<ProjectContractField> {

        public ContractFieldTableListView(String id) {
            super(id, new ContractFieldTableModel(ContractFieldTable.this.projectId));
        }

        @Override
        protected void populateItem(ListItem<ProjectContractField> item) {

        }

    }

}

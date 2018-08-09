package org.wickedsource.budgeteer.web.pages.contract.projectContractField.tableRowPanel.info;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;
import org.wickedsource.budgeteer.web.pages.contract.projectContractField.tableRowPanel.edit.FieldEditPanel;

import java.util.List;

public class FieldInfoPanel extends Panel {

    private final ProjectContractField field;

    private final List<ProjectContractField> fieldList;

    public FieldInfoPanel(String id, ProjectContractField field, List<ProjectContractField> fieldList) {
        super(id);

        this.field = field;
        this.fieldList = fieldList;

        this.addComponents();
    }

    public void addComponents() {
        add(new Label("title", field.getFieldName()));

        Button deleteButton = new Button("deleteButton") {
            @Override
            public void onSubmit() {
                fieldList.remove(field);
            }
        };

        AjaxLink editButton = new AjaxLink("editButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(getEditPanel());
            }
        };

        add(editButton);
        setOutputMarkupId(true);
        deleteButton.setDefaultFormProcessing(false);
        add(deleteButton);
    }


    public FieldEditPanel getEditPanel() {
        return null;
    }

}

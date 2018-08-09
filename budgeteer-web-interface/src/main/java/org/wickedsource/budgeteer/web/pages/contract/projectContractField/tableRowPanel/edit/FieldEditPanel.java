package org.wickedsource.budgeteer.web.pages.contract.projectContractField.tableRowPanel.edit;


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;

public class FieldEditPanel extends Panel {

    private boolean newField;

    private ProjectContractField field;

    public FieldEditPanel(String id, ProjectContractField field, boolean newField) {
        super(id);

        this.newField = newField;
        this.field = field;

        addComponents();
    }

    private void addComponents() {
        Form<ProjectContractField> form = new Form<>("addFieldForm", new Model<>(field));
        form.setOutputMarkupId(true);
        form.add(new TextField<>("title", new Model<>(field.getFieldName())));
        form.add(createSubmitButton("submitButton"));


        add(form);
    }

    private WebMarkupContainer createSubmitButton(String id) {
        Button submitButton = new Button(id) {

            @Override
            public void onSubmit() {
                super.onSubmit();
            }

        };

        return setSubmitButtonIcon(submitButton);
    }

    private WebMarkupContainer setSubmitButtonIcon(Button submitButton) {
        Label submitIcon = new Label("submitIcon");

        if(newField){
            submitIcon.add(new AttributeModifier("class", "fa fa-check"));
            submitIcon.add(new AttributeModifier("title", "Add this field"));
        }else{
            submitIcon.add(new AttributeModifier("class", "fa fa-plus-circle"));
            submitIcon.add(new AttributeModifier("title", "Save this field"));
        }

        submitButton.add(submitIcon);
        submitButton.setOutputMarkupId(true);
        submitIcon.setOutputMarkupId(true);

        return submitButton;
    }

}

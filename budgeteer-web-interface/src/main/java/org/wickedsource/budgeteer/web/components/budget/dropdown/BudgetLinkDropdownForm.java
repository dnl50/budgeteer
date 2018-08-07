package org.wickedsource.budgeteer.web.components.budget.dropdown;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.form.CsrfSecureForm;
import org.wickedsource.budgeteer.web.pages.budgets.monthreport.single.SingleBudgetMonthReportPage;
import org.wickedsource.budgeteer.web.pages.budgets.weekreport.single.SingleBudgetWeekReportPage;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetLinkDropdownForm extends CsrfSecureForm<BudgetOption> {

    private BudgetLinkType linkType;

    public enum BudgetLinkType {
        MONTHLY,
        WEEKLY
    }

    @SpringBean
    private BudgetService budgetService;

    public BudgetLinkDropdownForm(String id) {
        super(id);
        addComponents();
    }

    public BudgetLinkDropdownForm(String id, IModel model) {
        super(id, model);
        addComponents();

    }

    private void addComponents() {
        Model<BudgetOption> budgetOptionModel = new Model<>();
        setDefaultModel(budgetOptionModel);
        List<BudgetOption> budgetOptionList = budgetService
                .loadBudgetBaseDataForProject(BudgeteerSession.get().getProjectId())
                .stream().map(budget -> new BudgetOption(budget.getId(), budget.getName()))
                .collect(Collectors.toList());

        DropDownChoice budgetDropdown = new DropDownChoice<>(
                "budgetDropdown",
                budgetOptionModel,
                budgetOptionList,
                new BudgetOptionChoiceRenderer());
        budgetDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSubmit();
            }
        });
        add(budgetDropdown);
    }

    public void onSubmit() {
        PageParameters params = new PageParameters();
        params.add("id", getModel().getObject().getId());
        if (linkType != null && linkType.equals(BudgetLinkType.MONTHLY)) {
            setResponsePage(SingleBudgetMonthReportPage.class, params);
        } else if (linkType != null && linkType.equals(BudgetLinkType.WEEKLY)) {
            setResponsePage(SingleBudgetWeekReportPage.class, params);
        }
    }

    public BudgetLinkDropdownForm setLinkType(BudgetLinkType linkType) {
        this.linkType = linkType;
        return this;
    }

}

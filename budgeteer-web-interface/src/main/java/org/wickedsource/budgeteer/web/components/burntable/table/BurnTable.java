package org.wickedsource.budgeteer.web.components.burntable.table;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.imports.api.WorkingRecord;
import org.wickedsource.budgeteer.service.record.WorkingRecordFilter;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.burntable.filter.FilteredRecordsModel;
import org.wickedsource.budgeteer.web.components.money.BudgetUnitMoneyModel;
import org.wickedsource.budgeteer.web.components.money.MoneyLabel;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BurnTable extends Panel {

    public BurnTable(String id, FilteredRecordsModel model) {
        super(id, model);
        add(createList("recordList", model));
        add(new MoneyLabel("total", new BudgetUnitMoneyModel(new TotalBudgetModel(model))));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        Object payload = event.getPayload();
        if (payload instanceof WorkingRecordFilter) {
            WorkingRecordFilter filter = (WorkingRecordFilter) payload;
            FilteredRecordsModel model = (FilteredRecordsModel) getDefaultModel();
            model.setFilter(filter);
        }
    }

    private ListView<WorkingRecord> createList(String id, IModel<List<WorkingRecord>> model) {
        return new ListView<WorkingRecord>(id, model) {
            @Override
            protected void populateItem(ListItem<WorkingRecord> item) {
                item.add(new Label("budget", model(from(item.getModel()).getBudgetName())));
                item.add(new Label("person", model(from(item.getModel()).getPersonName())));
                item.add(new MoneyLabel("dailyRate", model(from(item.getModel()).getDailyRate())));
                item.add(new Label("date", model(from(item.getModel()).getDate())));
                item.add(new Label("hours", model(from(item.getModel()).getHours())));
                item.add(new MoneyLabel("burnedBudget", new BudgetUnitMoneyModel(model(from(item.getModel()).getBudgetBurned()))));
            }

            @Override
            protected ListItem<WorkingRecord> newItem(int index, IModel<WorkingRecord> itemModel) {
                // wrap model to work with LazyModel
                return super.newItem(index, new ClassAwareWrappingModel<WorkingRecord>(itemModel, WorkingRecord.class));
            }
        };
    }

}
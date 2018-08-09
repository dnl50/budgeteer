package org.wickedsource.budgeteer.web.pages.contract.projectContractField.fieldTable;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import java.util.List;

public class ContractFieldTableModel extends LoadableDetachableModel<List<ProjectContractField>> {

    @SpringBean
    private ProjectRepository projectRepository;

    private final long projectId;

    public ContractFieldTableModel(long projectId) {
        Injector.get().inject(this);

        this.projectId = projectId;
    }

    @Override
    protected List<ProjectContractField> load() {
        return projectRepository.findAllContractFields(projectId);
    }

}

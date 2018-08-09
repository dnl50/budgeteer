package org.wickedsource.budgeteer.persistence.project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {

    @Query("select pcf from ProjectContractField pcf where pcf.project.id = :projectId AND pcf.fieldName = :fieldName")
    public ProjectContractField findContractFieldByName(@Param("projectId") long projectId, @Param("fieldName") String fieldName);

    @Query("select p from ProjectEntity p join fetch p.contractFields where p.id = :id ")
    public ProjectEntity findById(@Param("id") long id);

    @Query("select pcf from ProjectContractField pcf where pcf.project.id = :projectId")
    public List<ProjectContractField> findAllContractFields(@Param("projectId") long projectId);

}

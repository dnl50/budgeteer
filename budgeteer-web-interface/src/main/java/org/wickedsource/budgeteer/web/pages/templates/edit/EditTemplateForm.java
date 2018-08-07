package org.wickedsource.budgeteer.web.pages.templates.edit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.form.CsrfSecureForm;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.templates.TemplatesPage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditTemplateForm extends CsrfSecureForm<TemplateFormInputDto> {

    @SpringBean
    private TemplateService service;

    private List<FileUpload> fileUploads = new ArrayList<>();

    private TemplateFormInputDto templateFormInputDto = new TemplateFormInputDto(BudgeteerSession.get().getProjectId());

    private long templateID;

    private static final AttributeModifier starChecked = new AttributeModifier("class", "btn bg-olive glyphicon glyphicon-star");
    private static final AttributeModifier starUnchecked = new AttributeModifier("class", "btn bg-olive glyphicon glyphicon-star-empty");


    public EditTemplateForm(String id, IModel<TemplateFormInputDto> formModel, long temID){
        super(id, formModel);
        this.templateID = temID;
        CustomFeedbackPanel feedback = new CustomFeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);

        add(feedback);
        FileUploadField fileUpload = new FileUploadField("fileUpload", new PropertyModel<>(this, "fileUploads"));
        fileUpload.setRequired(false);

        add(fileUpload);
        add(createCancelButton("backlink2"));
        add(DeleteTemplateButton("deleteButton"));
        add(DownloadFileButton("downloadFileButton"));

        add(new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    if(model(from(templateFormInputDto)).getObject().getName() == null){
                        error(getString("message.error.no.name"));
                    }
                    if(model(from(templateFormInputDto)).getObject().getType() == null){
                        error(getString("message.error.no.type"));
                    }
                    if(model(from(templateFormInputDto)).getObject().getName() != null && model(from(templateFormInputDto)).getObject().getName().length() > 128){
                        error(getString("message.error.name.too.long"));
                        target.add(feedback);
                        return;
                    }
                    if(model(from(templateFormInputDto)).getObject().getDescription() != null && model(from(templateFormInputDto)).getObject().getDescription().length() > 512){
                        error(getString("message.error.description.too.long"));
                        target.add(feedback);
                        return;
                    }
                    if(fileUploads != null && fileUploads.size() > 0){
                        ImportFile file = new ImportFile(fileUploads.get(0).getClientFileName(), fileUploads.get(0).getInputStream());
                        if(model(from(templateFormInputDto)).getObject().getName() != null && model(from(templateFormInputDto)).getObject().getType() != null){
                            service.editTemplate(BudgeteerSession.get().getProjectId(), templateID, file, model(from(templateFormInputDto)));
                            success(getString("message.success"));
                        }
                    }else if(model(from(templateFormInputDto)).getObject().getName() != null && model(from(templateFormInputDto)).getObject().getType() != null){
                        service.editTemplate(BudgeteerSession.get().getProjectId(), templateID, null, model(from(templateFormInputDto)));
                        success(getString("message.success"));
                    }
                } catch (IOException e) {
                    error(String.format(getString("message.ioError"), e.getMessage()));
                }  catch (IllegalArgumentException e) {
                    error(String.format(getString("message.importError"), e.getMessage()));
                }
                target.add(feedback);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        });

        templateFormInputDto.setName(service.getById(templateID).getName());
        templateFormInputDto.setDescription(service.getById(templateID).getDescription());
        templateFormInputDto.setType(service.getById(templateID).getType());
        templateFormInputDto.setDefault(service.getById(templateID).isDefault());

        AjaxLink checkBox = new AjaxLink("setAsDefault") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                if(templateFormInputDto.isDefault()){
                    EditTemplateForm.this.getModel().getObject().setDefault(false);
                    this.add(starUnchecked);
                    templateFormInputDto.setDefault(false);
                }else{
                    EditTemplateForm.this.getModel().getObject().setDefault(true);
                    this.add(starChecked);
                    templateFormInputDto.setDefault(true);
                }
                ajaxRequestTarget.add(this, this.getMarkupId());
            }
        };
        if(templateFormInputDto.isDefault()){
            checkBox.add(starChecked);
        }else{
            checkBox.add(starUnchecked);
        }
        checkBox.setOutputMarkupId(true);
        add(checkBox);
        add(new TextField<>("name", model(from(templateFormInputDto).getName())));
        add(new TextField<>("description", model(from(templateFormInputDto).getDescription())));
        DropDownChoice<ReportType> typeDropDown = new DropDownChoice<ReportType>("type", model(from(templateFormInputDto).getType()),
                new LoadableDetachableModel<List<ReportType>>() {
                    @Override
                    protected List<ReportType> load() {
                        List<ReportType> list = new ArrayList<>();
                        list.add(templateFormInputDto.getType());
                        for(ReportType E : ReportType.values()){
                            if(!E.equals(templateFormInputDto.getType())){
                                list.add(E);
                            }
                        }
                        return list;
                    }
                },
                new AbstractChoiceRenderer<ReportType>() {
                    @Override
                    public Object getDisplayValue(ReportType object) {
                        return object == null ? "Unnamed" : object.toString();
                    }
                }
        ){
            @Override
            public String getModelValue (){
                return null;
            }
        };
        typeDropDown.setNullValid(false);
        add(typeDropDown);
    }

    /**
     * Creates a button to download the template that is being edited.
     */
    private Link DownloadFileButton(String wicketId) {
        return new Link<Void>(wicketId) {
            @Override
            public void onClick() {
                XSSFWorkbook wb = service.getById(templateID).getWb();
                AbstractResourceStreamWriter streamWriter = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(OutputStream output) throws IOException {
                        wb.write(output);
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(streamWriter, service.getById(templateID).getName() + ".xlsx");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                HttpServletResponse response = (HttpServletResponse) getRequestCycle().getResponse().getContainerResponse();
                response.setContentType(null);
            }
        };
    }

    /**
     * Creates a back button.
     */
    private Link createCancelButton(String wicketId) {
        return new Link<Void>(wicketId) {
            @Override
            public void onClick() {
                ((EditTemplatePage)this.getPage()).goBack();
            }
        };
    }

    /**
     * Creates a button to delete the template that is being edited.
     */
    private Link DeleteTemplateButton(String wicketId) {
        return new Link<Void>(wicketId) {
            @Override
            public void onClick() {
                setResponsePage(new DeleteDialog(new Callable<Void>() {
                    @Override
                    public Void call(){
                        service.deleteTemplate(templateID);
                        ((EditTemplatePage)EditTemplateForm.this.getPage()).goBack();
                        return null;
                    }
                }, new Callable<Void>() {
                    @Override
                    public Void call(){
                        setResponsePage(new EditTemplatePage(TemplatesPage.class, getPage().getPageParameters(), templateID));
                        return null;
                    }
                }));
            }
        };
    }
}

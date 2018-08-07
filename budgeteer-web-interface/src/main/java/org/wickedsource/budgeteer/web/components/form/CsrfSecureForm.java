package org.wickedsource.budgeteer.web.components.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A {@link Form} that adds a hidden input to prevent CSRF attacks. Reads the CSRF token
 * from the request cookie with name {@value #CSRF_COOKIE_NAME} and adds a hidden input:
 *
 * <p> input type="hidden" name={@value #HIDDEN_CSRF_INPUT_NAME} value="<i>cookie value</i>" </p>
 *
 * The request parameter value can then be read from the request.
 *
 * @param <T>
 *          The type of the model object of the form.
 *
 * @see Form
 * @see Cookie
 */
public class CsrfSecureForm<T> extends Form<T> {

    private static final String HIDDEN_CSRF_INPUT_NAME = "_csrf";

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

    public CsrfSecureForm(String id) {
        super(id);
    }

    public CsrfSecureForm(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        if(isRootForm()) {
            HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();

            if(request != null && request.getCookies() != null) {
                List<Cookie> cookieList = Arrays.asList(request.getCookies());

                Optional<Cookie> csrfTokenCookie = cookieList.stream()
                        .filter(cookie -> CSRF_COOKIE_NAME.equals(cookie.getName()))
                        .findFirst();

                if(csrfTokenCookie.isPresent()) {
                    AppendingStringBuffer buffer = new AppendingStringBuffer().append(
                            "<input type=\"hidden\" name=\"")
                            .append(HIDDEN_CSRF_INPUT_NAME)
                            .append("\" value=\"")
                            .append(csrfTokenCookie.get().getValue())
                            .append("\" />");

                    getResponse().write(buffer);
                }
            }
        }

        super.onComponentTagBody(markupStream, openTag);
    }

}

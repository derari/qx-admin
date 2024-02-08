package org.cthul.qxadmin.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.cthul.qxadmin.data.*;

import java.util.List;

public abstract class QxController<T> {

    @Context
    UriInfo uri;

    @Context
    HttpHeaders headers;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        var view = getView();
        return getTarget("index")
                .data("meta", new QxMeta(uri.getBaseUri().toString(), uri.getRequestUri().getPath()))
                .data("schema", new QxSchema(List.of()))
                .data("view", view)
                .data("table", new QxTable<>(view.getRepository(), view.getType(), uri.getQueryParameters()))
                .render()
                ;
    }

    protected abstract QxRepository<T> getRepository();

    protected abstract QxType<T> getType();

    protected QxView<T> getView() {
        return new QxView<>(getClass().getSimpleName(), "bob", getType(), getRepository());
    }

    protected TemplateInstance getTarget(String page) {
        return getTargetTemplate(page).data("hx", isHx());
    }

    protected Template getTargetTemplate(String page) {
        var target = headers.getHeaderString("HX-Target");
        return QxTemplates.getTargetFragmentOld(page, target)
                .orElseThrow(() -> {
                    var response = Response.status(400)
                            .entity("not found: " + page + "/" + target)
                            .build();
                    return new WebApplicationException(response);
                });
    }

    protected boolean isHx() {
        String hx = headers.getHeaderString("HX-Request");
        return hx != null && hx.contains("true");
    }
}

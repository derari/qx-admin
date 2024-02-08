package org.cthul.qxadmin.dev;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.*;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.cthul.qxadmin.data.*;
import org.cthul.qxadmin.ui.QxTemplates;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Startup
public class DevUiResource implements Handler<RoutingContext> {

    QxSchema schema;

    @Inject
    void setSchema(DevSchemaProducer producer) {
        schema = producer.getSchema();
    }

    public void handle(RoutingContext routingContext) {
        Uni.createFrom().item(routingContext)
            .emitOn(Infrastructure.getDefaultWorkerPool())
            .map(this::render)
            .onFailure().invoke(t -> routingContext.response().setStatusCode(500).end())
            .subscribe().with(s -> routingContext.response().end(s));
    }

    @Transactional
    protected String render(RoutingContext routingContext) {
        if (routingContext.request().method() == HttpMethod.POST) {
            return renderPost(routingContext);
        }
        if (routingContext.request().method() == HttpMethod.PUT) {
            return renderUpdate(routingContext);
        }
        if (routingContext.request().method() == HttpMethod.DELETE) {
            return renderDelete(routingContext);
        }
        return getPage(routingContext)
                .data("meta", getMeta(routingContext))
                .data("schema", schema)
                .render();
    }

    protected String renderPost(RoutingContext routingContext) {
        return create(routingContext)
                .data("meta", getMeta(routingContext))
                .data("schema", schema)
                .render();
    }

    protected String renderUpdate(RoutingContext routingContext) {
        return update(routingContext)
                .data("meta", getMeta(routingContext))
                .data("schema", schema)
                .render();
    }

    protected String renderDelete(RoutingContext routingContext) {
        return delete(routingContext)
                .data("meta", getMeta(routingContext))
                .data("schema", schema)
                .render();
    }

    protected TemplateInstance create(RoutingContext routingContext) {
        var view = getView(routingContext);
        return getPage(routingContext, view);
    }

    protected <T> void create(RoutingContext routingContext, QxView<T> view) {
        var body = parseUrlEncoded(routingContext);
        var entity = view.getType().getNew(body);
        view.getRepository().persist(entity);
    }

    protected TemplateInstance update(RoutingContext routingContext) {
        var view = getView(routingContext);
        return update(routingContext, view);
    }

    private <T> TemplateInstance update(RoutingContext routingContext, QxView<T> view) {
        var id = routingContext.pathParams().getOrDefault("id", "");
        if (id.isBlank()) {
            throw new WebApplicationException("id", 404);
        }
        var entity = view.getRepository().get(id);
        var body = parseUrlEncoded(routingContext);
        view.getType().update(entity, body);
        return getPage(routingContext, view);
    }

    private Map<String, String> parseUrlEncoded(RoutingContext routingContext) {
        var future = new CompletableFuture<String>();
        routingContext.request().bodyHandler(buffer -> future.complete(buffer.toString()));
        return parseUrlEncoded(future.join());
    }

    private Map<String, String> parseUrlEncoded(String body) {
        Map<String, String> map = new HashMap<>();
        for (String pair: body.split("&")) {
            var i = pair.indexOf('=');
            if (i > 0) {
                var key = URLDecoder.decode(pair.substring(0, i), StandardCharsets.UTF_8);
                var value = URLDecoder.decode(pair.substring(i + 1), StandardCharsets.UTF_8);
                map.put(key, value);
            }
        }
        return map;
    }

    protected TemplateInstance delete(RoutingContext routingContext) {
        QxView<?> view = getView(routingContext);
        delete(routingContext, view);
        return getIndex(routingContext, view);
    }

    protected void delete(RoutingContext routingContext, QxView<?> view) {
        var id = routingContext.pathParams().getOrDefault("id", "");
        if (id.isBlank()) {
            throw new WebApplicationException("id", 404);
        }
        view.getRepository().delete(id);
    }

    private QxView<?> getView(RoutingContext routingContext) {
        var resource = routingContext.pathParams().getOrDefault("resource", "");
        if (resource.isBlank()) {
            throw new WebApplicationException("resource", 404);
        }
        var view = schema.getViews().get(resource);
        if (view == null) throw new WebApplicationException(resource, 404);
        return view;
    }

    protected QxMeta getMeta(RoutingContext routingContext) {
        var home = routingContext.currentRoute().getPath();
        var self = URI.create(routingContext.request().absoluteURI()).getPath();
        var i = self.indexOf(home);
        if (i > 0) home = self.substring(0, i + home.length());
        return new QxMeta(home, self);
    }

    protected TemplateInstance getPage(RoutingContext routingContext) {
        var resource = routingContext.pathParams().getOrDefault("resource", "");
        if (resource.isBlank()) {
            return getHome(routingContext);
        }
        var view = schema.getViews().get(resource);
        if (view == null) throw new WebApplicationException(resource, 404);
        return getPage(routingContext, view);
    }

    private TemplateInstance getPage(RoutingContext routingContext, QxView<?> view) {
        var id = routingContext.pathParams().getOrDefault("id", "");
        if (id.isBlank()) {
            return getIndex(routingContext, view);
        }
        if ("new".equals(id)) {
            return getNewPage(routingContext, view);
        }
        var operation = routingContext.pathParams().getOrDefault("operation", "");
        return getEntityPage(routingContext, view, id, operation);
    }

    protected TemplateInstance getHome(RoutingContext routingContext) {
        var headers = routingContext.request().headers();
        return getTargetTemplate("home", headers);
    }

    protected <T> TemplateInstance getIndex(RoutingContext routingContext, QxView<T> view) {
        var headers = routingContext.request().headers();
        return getTargetTemplate("index", headers)
                .data("view", view)
                .data("table", new QxTable<>(view.getRepository(), view.getType(), new MultivaluedHashMap<>()));
    }

    protected <T> TemplateInstance getNewPage(RoutingContext routingContext, QxView<T> view) {
        var headers = routingContext.request().headers();
        return getTargetTemplate("new", headers)
                .data("view", view)
                .data("entity", new QxEntity<>(view.getType().getNew(), view.getType()));
    }

    protected <T> TemplateInstance getEntityPage(RoutingContext routingContext, QxView<T> view, String id, String operation) {
        if (operation.isBlank()) operation = "show";
        var headers = routingContext.request().headers();
        return getTargetTemplate(operation, headers)
                .data("view", view)
                .data("entity", new QxEntity<>(view.getRepository().get(id), view.getType()));
    }

    protected TemplateInstance getTargetTemplate(String page, MultiMap headers) {
        var target = headers.get("HX-Target");
        return QxTemplates.getTargetFragment(page, target)
                .orElseThrow(() -> new WebApplicationException(page + "/" + target, 400))
                .data("hx", isHx(headers));
    }

    protected boolean isHx(MultiMap headers) {
        String hx = headers.get("HX-Request");
        return hx != null && hx.contains("true");
    }
}

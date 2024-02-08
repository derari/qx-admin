package org.cthul.qxadmin.dev;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedHashMap;
import org.cthul.qxadmin.data.*;
import org.cthul.qxadmin.ui.QxTemplates;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;

@Singleton
public class DevUiHelper {

    @Transactional
    public <T> T request(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}

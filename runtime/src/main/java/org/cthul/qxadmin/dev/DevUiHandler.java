package org.cthul.qxadmin.dev;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.vertx.core.Handler;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Consumer;

public class DevUiHandler implements Handler<RoutingContext> {

    public static final String PATH = "x-admin";
    @SuppressWarnings("java:S1075")
    private static final String SUB_PATH = "/" + PATH;

    DevUiResource resource;

    public void handle(RoutingContext routingContext) {
        if (resource == null) {
            try (InstanceHandle<DevUiResource> handle = Arc.container().instance(DevUiResource.class)) {
                resource = handle.get();
            }
        }
        resource.handle(routingContext);
    }

    public static class PathRegex implements Consumer<Route> {
        @Override
        public void accept(Route route) {
            route.pathRegex(SUB_PATH + "/(?<resource>[^/]+)(/(?<id>[^/]+)(/(?<operation>[^/]+))?)?");
        }
    }
}

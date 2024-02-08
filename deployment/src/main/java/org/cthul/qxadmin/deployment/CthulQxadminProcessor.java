package org.cthul.qxadmin.deployment;

import io.quarkus.arc.deployment.*;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.*;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.logging.Log;
import io.quarkus.vertx.http.deployment.NonApplicationRootPathBuildItem;
import io.quarkus.vertx.http.deployment.RouteBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Entity;
import org.cthul.qxadmin.dev.*;
import org.jboss.jandex.*;

import java.util.ArrayList;
import java.util.List;

class CthulQxadminProcessor {

    private static final String FEATURE = "qxadmin";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    CardPageBuildItem devPage(NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem) {
        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();
        cardPageBuildItem.addPage(Page.externalPageBuilder("Qx Admin UI")
                .url(nonApplicationRootPathBuildItem.resolvePath(DevUiHandler.PATH))
                .isHtmlContent());
        return cardPageBuildItem;
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    @Record(ExecutionTime.STATIC_INIT)
    void devUi(CombinedIndexBuildItem index, NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem, QxAdminRecorder recorder,
               BuildProducer<RouteBuildItem> routes,
               BuildProducer<SyntheticBeanBuildItem> synthBeans,
               BuildProducer<AdditionalBeanBuildItem> addBeans) {
        List<String> views = new ArrayList<>();

        for (AnnotationInstance at: index.getIndex().getAnnotations(Entity.class)) {
            if (!AnnotationTarget.Kind.CLASS.equals(at.target().kind())) continue;
            ClassInfo info = at.target().asClass();
            Log.infof("Found class %s", info);
            views.add(info.name().toString());
        }

        SyntheticBeanBuildItem schema = SyntheticBeanBuildItem
                .configure(DevSchemaProducer.class)
                .scope(ApplicationScoped.class)
                .runtimeValue(recorder.devUiSchema(views))
                .unremovable()
                .done();
        synthBeans.produce(schema);

        addBeans.produce(AdditionalBeanBuildItem.unremovableOf(DevUiResource.class));

        DevUiHandler handler = new DevUiHandler();

        routes.produce(nonApplicationRootPathBuildItem.routeBuilder()
                .route(DevUiHandler.PATH)
                .handler(handler)
                .displayOnNotFoundPage()
                .build());

        routes.produce(nonApplicationRootPathBuildItem.routeBuilder()
                .routeFunction(DevUiHandler.PATH, new DevUiHandler.PathRegex())
                .handler(handler)
                .build());
    }
}

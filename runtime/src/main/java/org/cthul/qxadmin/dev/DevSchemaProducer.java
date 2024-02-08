package org.cthul.qxadmin.dev;

import io.quarkus.logging.Log;
import org.cthul.qxadmin.data.QxSchema;
import org.cthul.qxadmin.data.QxView;

import java.util.List;
import java.util.stream.Collectors;

public class DevSchemaProducer {

    private final List<String> classes;

    public DevSchemaProducer(List<String> classes) {
        this.classes = classes;
    }

    public QxSchema getSchema() {
        List<QxView<?>> views = classes.stream().map(this::view).collect(Collectors.toList());
        return new QxSchema(views);
    }

    private <T> QxView<T> view(String className) {
        Log.infof("Creating view %s", className);
        GenericQxFactory factory = new GenericQxFactory();
        return new QxView<>(className, className, factory.newType(className), factory.newRepository(className));
    }
}

package org.cthul.qxadmin.dev;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.List;

@Recorder
public class QxAdminRecorder {

    public RuntimeValue<DevSchemaProducer> devUiSchema(List<String> classes) {
        return new RuntimeValue<>(new DevSchemaProducer(classes));
    }
}

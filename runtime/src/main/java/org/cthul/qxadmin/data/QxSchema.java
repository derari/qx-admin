package org.cthul.qxadmin.data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QxSchema {

    private final Map<String, QxView<?>> views;

    public QxSchema(Collection<QxView<?>> views) {
        this.views = views.stream()
                .collect(Collectors.toMap(QxView::getPath, Function.identity()));
    }

    public Map<String, QxView<?>> getViews() {
        return views;
    }
}

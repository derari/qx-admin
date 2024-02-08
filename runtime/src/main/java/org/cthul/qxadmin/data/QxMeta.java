package org.cthul.qxadmin.data;

import java.net.URI;

public class QxMeta {

    private final String home;
    private final String self;

    public QxMeta(String home, String self) {
        this.home = home;
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public String getHome() {
        return home;
    }
}

package org.cthul.qxadmin.ui;

import io.quarkus.qute.*;

import java.util.Optional;

public class QxTemplates {

    public static Optional<Template> get(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return Optional.ofNullable(Qute.engine().getTemplate( "qx/" + id));
    }

    public static Optional<Template> get(String id, String fragment) {
        return get(id).map(t -> t.getFragment(fragment));
    }

    public static Optional<Template> getTargetFragmentOld(String page, String target) {
        if (target == null || target.isBlank()) {
            return get(page);
        }
        var parts = target.split("-", 3);
        if (parts.length > 1) {
            var fragment = get(parts[0], parts[1]);
            if (fragment.isPresent()) return fragment;
        }
        return get(page, parts[0])
                .or(() -> get(parts[0]))
                .or(() -> get(page));
    }

    public static Optional<Template> getTargetFragment(String page, String target) {
        if (target == null || target.isBlank()) {
            return get(page);
        }
        var parts = target.split("-", 3);
        if (parts.length == 1) {
            return content(get(parts[0]))
                    .or(() -> getFragmentOrContent(page, parts[0]));
        }
        return getFragmentOrContent(parts[0], parts[1])
                .or(() -> getFragmentOrContent(page, parts[0]));
    }

    private static Optional<Template> getFragmentOrContent(String page, String fragment) {
        var targetTemplate = get(page);
        if (targetTemplate.isPresent()) {
            return fragment(targetTemplate, fragment).or(() -> content(targetTemplate));
        }
        return Optional.empty();
    }

    private static Optional<Template> content(Optional<Template> template) {
        return fragment(template, "content").or(() -> template);
    }

    private static Optional<Template> fragment(Optional<Template> template, String fragment) {
        if (fragment == null || fragment.isBlank()) return template;
        return template.map(f -> f.getFragment(fragment));
    }

    private QxTemplates() {
    }
}

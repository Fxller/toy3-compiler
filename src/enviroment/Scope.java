package enviroment;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Scope {
    private Map<Group, Map<String, String>>scope;
    private Scope parent;
    public enum Group {
        VAR, FUN
    }

    protected Scope(Scope parent) {
        this.scope = new EnumMap<>(Group.class);
        this.parent = parent;

        for (Group kind : Group.values()) {
            this.scope.put(kind, new HashMap<>());
        }
    }

    public Map<Group, Map<String, String>> getScope() {
        return scope;
    }

    public void setScope(Map<Group, Map<String, String>> scope) {
        this.scope = scope;
    }

    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Scope{" +
                "scope=" + scope +
                ", parent=" + parent +
                '}';
    }
}

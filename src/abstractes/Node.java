package abstractes;

import interfaces.NodeInterface;
import enviroment.Scope;

public abstract class Node implements NodeInterface {
    private String type = "notype";
    private Scope scope;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

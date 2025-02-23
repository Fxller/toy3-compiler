package interfaces;

import enviroment.Scope;

public interface NodeInterface {
    void accept(Visitor visitor);

    Scope getScope();

    void setScope(Scope scope);

    void setType(String type);

    String getType();
}

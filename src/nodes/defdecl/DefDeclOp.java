package nodes.defdecl;

import nodes.body.BodyOp;
import nodes.expr.Identifier;
import nodes.pardecl.ParDeclOp;
import abstractes.Node;
import interfaces.Visitor;

import java.util.List;

public class DefDeclOp extends Node {
    private Identifier id;
    private List<ParDeclOp> parDecls;
    private String type;
    private BodyOp body;

    public DefDeclOp(Identifier id, List<ParDeclOp> parDecls, String type, BodyOp body) {
        this.id = id;
        this.parDecls = parDecls;
        this.type = type;
        this.body = body;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public List<ParDeclOp> getParDecls() {
        return parDecls;
    }

    public void setParDecls(List<ParDeclOp> parDecls) {
        this.parDecls = parDecls;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BodyOp getBody() {
        return body;
    }

    public void setBody(BodyOp body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "DefDeclOp{" +
                "id=" + id +
                ", parDecls=" + parDecls +
                ", type='" + type + '\'' +
                ", body=" + body +
                '}';
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

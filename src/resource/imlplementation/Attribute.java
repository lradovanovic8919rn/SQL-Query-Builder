package resource.imlplementation;

import resource.DBNode;
import resource.DBNodeComposite;
import resource.enums.AttributeType;

public class Attribute extends DBNodeComposite {

    private AttributeType attributeType;
    private int length;
    private Attribute inRelationWith;

    public Attribute(String name, DBNode parent, AttributeType attributeType, int length) {
        super(name, parent);
        this.attributeType = attributeType;
        this.length = length;
    }

    public Attribute(String name, DBNode parent) {
        super(name, parent);
    }

    @Override
    public void AddChild(DBNode child) {
        if (child != null && child instanceof AttributeConstraint){
            AttributeConstraint attributeConstraint = (AttributeConstraint) child;
            this.getChildren().add(attributeConstraint);
        }
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public int getLength() {
        return length;
    }

    public Attribute getInRelationWith() {
        return inRelationWith;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setInRelationWith(Attribute inRelationWith) {
        this.inRelationWith = inRelationWith;
    }
}

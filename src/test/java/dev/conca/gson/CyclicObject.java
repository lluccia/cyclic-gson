package dev.conca.gson;

public class CyclicObject {
    private String field1;
    private CyclicObject cyclicObject;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public CyclicObject getCyclicObject() {
        return cyclicObject;
    }

    public void setCyclicObject(CyclicObject cyclicObject) {
        this.cyclicObject = cyclicObject;
    }
}

package com.dmall.managed.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoupeng on 16/2/26.
 */
public class Service implements Serializable {
    private Node node;

    private String name;
    private String desc;

    private List<Operation> operations = new ArrayList<Operation>();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public void add(Operation operation){
        this.operations.add(operation);
    }
}

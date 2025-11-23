package com.spike.security.model;

import java.util.Objects;

public class Document {
    private String owner;

    public Document() {
    }

    public Document(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Document document = (Document) object;
        return Objects.equals(owner, document.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }
}

package com.sdi.hostedin.feature.guest.explore;

import java.util.Objects;

public class PlaceResult {
    private String name;
    private String address;
    private String id;

    public PlaceResult(String name, String address, String id) {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    public PlaceResult(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceResult that = (PlaceResult) o;
        return Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, id);
    }
}

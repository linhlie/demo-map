package com.example.app.kidstracking.model;

public class NotiDTO {
    private String name;

    public NotiDTO(String name) {
        this.name = name;
    }

    public NotiDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NotiDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}

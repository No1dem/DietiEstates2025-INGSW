package it.unina.dietiEstates25.model;

import java.util.Objects;

public class Foto {
    private int id;
    private String name;
    private byte[] data; // Per il BLOB
    private String path;

    public Foto(String path){
        this.path = path;
    }

    // Costruttore per immagine come BLOB
    public Foto(int id, String name, byte[] data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    // Costruttore per immagine con percorso
    public Foto(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public  String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foto image = (Foto) o;
        return id == image.id && Objects.equals(name, image.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

package com.darujo.networkstorageclient.controller;

public class Layer {


    private final String layerName;

    private final boolean file;
    private String guid;

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Layer(String layerName, boolean isFile, String guid) {
        this.layerName = layerName;
        this.file = isFile;
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public String getLayerName() {
        return layerName;
    }


    public boolean isFile() {
        return file;
    }


    @Override
    public String toString() {
        return this.layerName + " " + this.guid;
    }

}
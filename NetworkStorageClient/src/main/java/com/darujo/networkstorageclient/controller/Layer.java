package com.darujo.networkstorageclient.controller;

public class Layer {


    private final String layerName;

    private final boolean file;

    public Layer(String layerName, boolean isFile) {
        this.layerName = layerName;
        this.file = isFile;
    }

    public String getLayerName() {
        return layerName;
    }

//    public void setLayerName(String layerName) {
//        this.layerName = layerName;
//    }

    public boolean isFile() {
        return file;
    }


    @Override
    public String toString() {
        return this.layerName;
    }

}
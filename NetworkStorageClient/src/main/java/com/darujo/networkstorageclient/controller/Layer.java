package com.darujo.networkstorageclient.controller;

public class Layer {


    private String layerName;

    private boolean selected;

    public Layer(String layerName, boolean selected) {
        this.layerName = layerName;
        this.selected = selected;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return this.layerName + this.selected;
    }

}
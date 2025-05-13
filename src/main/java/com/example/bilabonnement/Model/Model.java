package com.example.bilabonnement.Model;

public class Model {
    private Integer modelId;
    private String modelName;
    private Integer brandId;
    private String brandName;

    public Model() {}

    public Model(Integer modelId, String modelName, Integer brandId) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.brandId = brandId;
    }

    public Model(String modelName, Integer brandId) {
        this.modelName = modelName;
        this.brandId = brandId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    @Override
    public String toString() {
        return "Model{" +
                "modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", brandId=" + brandId +
                '}';
    }
} 
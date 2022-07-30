package com.example.e_commerce_app.models;

public class AdressModel {

    String userAdress;
    boolean isSelected;

    public AdressModel() {
    }

    public AdressModel(String userAdress, boolean isSelected) {
        this.userAdress = userAdress;
        this.isSelected = isSelected;
    }

    public String getUserAdress() {
        return userAdress;
    }

    public void setUserAdress(String userAdress) {
        this.userAdress = userAdress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package com.example.project;

public class CartModel {

String Name;
String rating,id;
String image;


public CartModel(String name, String rating, String id, String image) {
    this.Name = name;
    this.rating = rating;
    this.id = id;
    this.image = image;
}

public String getName() {
    return Name;
}

public void setName(String name) {
    Name = name;
}

public String getRating() {
    return rating;
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public void setRating(String rating) {
    rating = rating;
}

public String  getImage() {
    return image;
}

public void setImage(String image) {
    this.image = image;
}}



package com.example.pagetrade;

public class CartModel {
    private String key,bookId,image,bookName, author, buyerId, sellerId, price, number, type, mail;
    public CartModel() {
    }

    public CartModel(String key,String bookId, String image,String bookName, String author, String price,String number,String buyerId, String sellerId, String mail, String type) {
        this.key = key;
        this.bookId = bookId;
        this.image = image;
        this.bookName = bookName;
        this.author = author;
        this.number = number;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.mail = mail;
        this.price = price;
        this.type = type;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBuyerId() {
        return buyerId;
    }
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}

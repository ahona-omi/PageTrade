package com.example.pagetrade;

public class BookItemModel {
    private String bookId,catKey,image,bookName, author, description, price, sellerName, sellerPhone, sellerMail, uid;

    public BookItemModel() {
    }
    public BookItemModel(String bookId,String author, String catKey, String description, String image, String price, String sellerMail, String sellerName, String sellerPhone, String bookName,  String uid) {
        this.bookId = bookId;
        this.image = image;
        this.bookName = bookName;
        this.catKey = catKey;
        this.author = author;
        this.description = description;
        this.price = price;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sellerMail = sellerMail;
        this.uid = uid;
    }

    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCatKey() {
        return catKey;
    }
    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }
    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerMail() {
        return sellerMail;
    }
    public void setSellerMail(String sellerMail) {
        this.sellerMail = sellerMail;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}

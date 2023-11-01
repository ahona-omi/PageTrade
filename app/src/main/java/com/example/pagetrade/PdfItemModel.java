package com.example.pagetrade;

public class PdfItemModel {
    private String pdfId,catKey,pdfUrl,image,pdfName, author, description, price, sellerName, sellerPhone, sellerMail, uid;
    long downloadCnt;
    public PdfItemModel() {
    }
    public PdfItemModel(String pdfId,String catKey, String image, String pdfUrl, String pdfName, String author, String description, String price, String sellerName, String sellerPhone, String sellerMail, String uid) {
        this.pdfId = pdfId;
        this.catKey = catKey;
        this.image = image;
        this.pdfUrl = pdfUrl;
        this.pdfName = pdfName;
        this.author = author;
        this.description = description;
        this.price = price;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sellerMail = sellerMail;
        this.uid = uid;
    }

    public String getPdfId() {
        return pdfId;
    }
    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getCatKey() {
        return catKey;
    }
    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }
    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getPdfName() {
        return pdfName;
    }
    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
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

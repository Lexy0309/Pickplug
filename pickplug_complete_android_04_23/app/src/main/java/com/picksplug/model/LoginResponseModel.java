package com.picksplug.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LENOVO on 6/27/2018.
 */

public class LoginResponseModel {

        @SerializedName("Login")
        @Expose
        private Login login;

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

    public class Login {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("user")
        @Expose
        private User user;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public class User {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("FullName")
        @Expose
        private String fullName;
        @SerializedName("Password")
        @Expose
        private String password;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("PhoneNumber")
        @Expose
        private String phoneNumber;
        @SerializedName("Birthday")
        @Expose
        private String birthday;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("Photo")
        @Expose
        private String photo;
        @SerializedName("session")
        @Expose
        private String session;
        @SerializedName("Facebooktoken")
        @Expose
        private String facebooktoken;
        @SerializedName("TwitterToken")
        @Expose
        private String twitterToken;
        @SerializedName("PickPush")
        @Expose
        private String pickPush;
        @SerializedName("SportsPush")
        @Expose
        private String sportsPush;
        @SerializedName("FavoritesPush")
        @Expose
        private String favoritesPush;
        @SerializedName("PickMail")
        @Expose
        private String pickMail;
        @SerializedName("SportsMail")
        @Expose
        private String sportsMail;
        @SerializedName("FavoritesMail")
        @Expose
        private String favoritesMail;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public String getFacebooktoken() {
            return facebooktoken;
        }

        public void setFacebooktoken(String facebooktoken) {
            this.facebooktoken = facebooktoken;
        }

        public String getTwitterToken() {
            return twitterToken;
        }

        public void setTwitterToken(String twitterToken) {
            this.twitterToken = twitterToken;
        }

        public String getPickPush() {
            return pickPush;
        }

        public void setPickPush(String pickPush) {
            this.pickPush = pickPush;
        }

        public String getSportsPush() {
            return sportsPush;
        }

        public void setSportsPush(String sportsPush) {
            this.sportsPush = sportsPush;
        }

        public String getFavoritesPush() {
            return favoritesPush;
        }

        public void setFavoritesPush(String favoritesPush) {
            this.favoritesPush = favoritesPush;
        }

        public String getPickMail() {
            return pickMail;
        }

        public void setPickMail(String pickMail) {
            this.pickMail = pickMail;
        }

        public String getSportsMail() {
            return sportsMail;
        }

        public void setSportsMail(String sportsMail) {
            this.sportsMail = sportsMail;
        }

        public String getFavoritesMail() {
            return favoritesMail;
        }

        public void setFavoritesMail(String favoritesMail) {
            this.favoritesMail = favoritesMail;
        }

    }

}
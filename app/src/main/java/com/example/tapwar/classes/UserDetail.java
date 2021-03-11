package com.example.tapwar.classes;

import android.net.Uri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserDetail {


    private String personName;
    private String personEmail;
    private String personId;
    private Uri personPhoto;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Uri getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(Uri personPhoto) {
        this.personPhoto = personPhoto;
    }

    public String toJson() {
        return new GsonBuilder().create().toJson(this, UserDetail.class);
    }

    public static UserDetail toObject(String json) {

        return new GsonBuilder().create().fromJson(json, UserDetail.class);
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "personName='" + personName + '\'' +
                ", personEmail='" + personEmail + '\'' +
                ", personId='" + personId + '\'' +
                ", personPhoto=" + personPhoto +
                '}';
    }
}

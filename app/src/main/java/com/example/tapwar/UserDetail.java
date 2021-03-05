package com.example.tapwar;

import android.net.Uri;

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
}

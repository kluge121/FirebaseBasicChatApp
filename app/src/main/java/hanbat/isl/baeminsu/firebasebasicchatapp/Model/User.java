package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

import java.io.Serializable;

/**
 * Created by baeminsu on 2018. 1. 6..
 */

public class User implements Serializable {

    private String uid;
    private String name;
    private String profileUrl;
    private String email;
    private String message;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}

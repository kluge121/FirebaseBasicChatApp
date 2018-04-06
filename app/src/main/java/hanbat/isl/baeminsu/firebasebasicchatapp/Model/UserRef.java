package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by baeminsu on 2018. 1. 8..
 */

public class UserRef {

    private DocumentReference User;
    private String email;
    private String name;

    public UserRef() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setUser(DocumentReference user) {
        User = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DocumentReference getUser() {

        return User;
    }

    public String getEmail() {
        return email;
    }
}

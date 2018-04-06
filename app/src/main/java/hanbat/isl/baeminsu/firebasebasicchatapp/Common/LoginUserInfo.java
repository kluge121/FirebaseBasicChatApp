package hanbat.isl.baeminsu.firebasebasicchatapp.Common;

import com.google.firebase.firestore.DocumentReference;

import hanbat.isl.baeminsu.firebasebasicchatapp.Model.User;

/**
 * Created by baeminsu on 2018. 1. 8..
 */

public class LoginUserInfo {

    private static User loginUserInfo;
    private static DocumentReference loginUserRef;

    public static void setLoginUserInfo(User loginUserInfo) {
        LoginUserInfo.loginUserInfo = loginUserInfo;
    }

    public static DocumentReference getLoginUserRef() {
        return loginUserRef;
    }

    public static void setLoginUserRef(DocumentReference loginUserRef) {
        LoginUserInfo.loginUserRef = loginUserRef;
    }

    public static User getLoginUserInfo() {
        return loginUserInfo;
    }

    public static boolean checkProfile() {
        return loginUserInfo.getProfileUrl() != null;
    }
}

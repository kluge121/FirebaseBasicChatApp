package hanbat.isl.baeminsu.firebasebasicchatapp.Model;

/**
 * Created by baeminsu on 2018. 1. 17..
 */

public class InviteListUser extends User {

    private boolean check = false;

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {

        return check;
    }


    public static InviteListUser userToInviteSelectUser(User user) {

        InviteListUser returnUser = new InviteListUser();

        returnUser.setUid(user.getUid());
        returnUser.setProfileUrl(user.getProfileUrl());
        returnUser.setName(user.getName());
        returnUser.setMessage(user.getMessage());
        returnUser.setEmail(user.getEmail());
        returnUser.setCheck(false);

        return returnUser;
    }

}

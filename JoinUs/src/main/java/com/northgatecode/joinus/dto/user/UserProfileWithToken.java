package com.northgatecode.joinus.dto.user;

/**
 * Created by qianliang on 3/3/2016.
 */
public class UserProfileWithToken {
    private UserProfile userProfile;
    private UserToken userToken;

    public UserProfileWithToken(){}

    public UserProfileWithToken(UserProfile userProfile, UserToken userToken) {
        this.userProfile = userProfile;
        this.userToken = userToken;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
}

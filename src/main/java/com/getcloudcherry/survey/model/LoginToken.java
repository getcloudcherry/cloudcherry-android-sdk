package com.getcloudcherry.survey.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshdubey on 8/10/16.
 */
public class LoginToken {
    public String access_token;
    public String token_type;
    public long expires_in;
    public String userName;
    public String email;
    public String primaryRole;
    public String managedBy;
    @SerializedName(".issued")
    public String issued;
    @SerializedName(".expires")
    public String expires;
}

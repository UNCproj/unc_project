/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unc.helpers;

import java.math.BigInteger;
import java.util.List;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
/**
 *
 * @author artem
 */
public class VkOauthLogin {
    
    private String clientID = "5388232";
    private String clientSecret = "UcKEKFIuI0Qh7in1sBBb";
    private String redirectURI;
    private String code;
    private String authKey;
    OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
    
    public VkOauthLogin(String redirectURI) throws OAuthSystemException{
        this.redirectURI = redirectURI;
    }
    
    void setAuthKey(String authKey){
        this.authKey = authKey;
    }

    /**
     * @return the redirectURI
     */
    public String getRedirectURI() {
        return redirectURI;
    }

    /**
     * @param redirectURI the redirectURI to set
     */
    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the authKey
     */
    public String getAuthKey() {
        return authKey;
    }

    /**
     * @return the clientID
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * @return the clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }
    
}

package com.spike.spring.edgeservice.nimbusds;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TestNimbusOAuth2 {

    // https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/oauth/authorization-request
    @Order(1)
    @Test
    public void testAuthorizationRequestUsingCodeFlow() throws URISyntaxException, ParseException {
        // The authorisation endpoint of the server
        URI authzEndpoint = new URI("https://c2id.com/authz");

        // The client identifier provisioned by the server
        ClientID clientID = new ClientID("123");

        // The requested scope values for the token
        Scope scope = new Scope("read", "write");

        // The client callback URI, typically pre-registered with the server
        URI callback = new URI("https://client.com/callback");

        // Generate random state string for pairing the response to the request
        State state = new State();

        // Build the request
        AuthorizationRequest request = new AuthorizationRequest.Builder(
                new ResponseType(ResponseType.Value.CODE), clientID)
                .scope(scope)
                .state(state)
                .redirectionURI(callback)
                .endpointURI(authzEndpoint)
                .build();

        // Use this URI to send the end-user's browser to the server
        URI requestURI = request.toURI();
        System.out.println(requestURI.toString());

        //
        // client callback endpoint
        //

        // Parse the authorisation response from the callback URI
        String mockCallbackURI = "https://client.com/callback?code=123456&state=" + state;
        AuthorizationResponse response = AuthorizationResponse.parse(new URI(mockCallbackURI));

        // Check the returned state parameter, must match the original
        if (!state.equals(response.getState())) {
            // Unexpected or tampered response, stop!!!
            return;
        }

        if (!response.indicatesSuccess()) {
            // The request was denied or some error occurred
            AuthorizationErrorResponse errorResponse = response.toErrorResponse();
            System.out.println(errorResponse.getErrorObject());
            return;
        }

        AuthorizationSuccessResponse successResponse = response.toSuccessResponse();

        // Retrieve the authorisation code, to be used later to exchange the code for
        // an access token at the token endpoint of the server
        AuthorizationCode code = successResponse.getAuthorizationCode();
        Assertions.assertThat(code).isNotNull();
        System.out.println(code);
    }


    @Order(2)
    @Test
    public void testTokenRequestUsingCredentialClient() throws URISyntaxException, IOException, ParseException {
        AuthorizationCode mockCode = new AuthorizationCode("123456");
        // Construct the code grant from the code obtained from the authz endpoint
        // and the original callback URI used at the authz endpoint
        URI callback = new URI("https://client.com/callback");
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(mockCode, callback);

        // The credentials to authenticate the client at the token endpoint
        ClientID clientID = new ClientID("123");
        Secret clientSecret = new Secret("secret");
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);

        // The token endpoint
        URI tokenEndpoint = new URI("https://c2id.com/token");

        // Make the token request
        TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, codeGrant);
        HTTPResponse mockResponse = new HTTPResponse(HTTPResponse.SC_OK);
        mockResponse.setContentType("application/json");
        mockResponse.setContent("""
                {
                  "access_token"      : "2YotnFZFEjr1zCsicMWpAA",
                  "token_type"        : "Bearer",
                  "expires_in"        : 3600,
                  "refresh_token"     : "tGzv3JOkF0XG5Qx2TlKWIA",
                  "example_parameter" : "example_value"
                }""");
        TokenResponse response = //TokenResponse.parse(request.toHTTPRequest().send());
                TokenResponse.parse(mockResponse);

        if (!response.indicatesSuccess()) {
            // We got an error response...
            TokenErrorResponse errorResponse = response.toErrorResponse();
        }

        AccessTokenResponse successResponse = response.toSuccessResponse();

        // Get the access token, the server may also return a refresh token
        AccessToken accessToken = successResponse.getTokens().getAccessToken();
        RefreshToken refreshToken = successResponse.getTokens().getRefreshToken();
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(refreshToken).isNotNull();
        System.out.println(accessToken);
        System.out.println(refreshToken);
    }
}

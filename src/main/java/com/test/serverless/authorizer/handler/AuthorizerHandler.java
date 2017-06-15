package com.test.serverless.authorizer.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.test.serverless.authorizer.model.AuthPolicy;
import com.test.serverless.authorizer.model.TokenAuthorizerContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizerHandler implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {

	@Override
	public AuthPolicy handleRequest(TokenAuthorizerContext request, Context context) {

		log.info("inbound request  "+request.toString());
		String token = request.getAuthorizationToken();
		
		 DecodedJWT jwt =null;
		try {
			  jwt = JWT.decode(token);
		} catch (JWTVerificationException exception){
			exception.printStackTrace();
		    //Invalid signature/claims
		}

        String principalId = jwt.getSubject();

        // if the client token is not recognized or invalid
        // you can send a 401 Unauthorized response to the client by failing like so:
        // throw new RuntimeException("Unauthorized");

        // if the token is valid, a policy should be generated which will allow or deny access to the client

        // if access is denied, the client will receive a 403 Access Denied response
        // if access is allowed, API Gateway will proceed with the back-end integration configured on the method that was called

    	String methodArn = request.getMethodArn();
    	String[] arnPartials = methodArn.split(":");
    	String region = arnPartials[3];
    	String awsAccountId = arnPartials[4];
    	String[] apiGatewayArnPartials = arnPartials[5].split("/");
    	String restApiId = apiGatewayArnPartials[0];
    	String stage = apiGatewayArnPartials[1];
    	String httpMethod = apiGatewayArnPartials[2];
    	String resource = ""; // root resource
    	if (apiGatewayArnPartials.length == 4) {
    		resource = apiGatewayArnPartials[3];
    	}

        // this function must generate a policy that is associated with the recognized principal user identifier.
        // depending on your use case, you might store policies in a DB, or generate them on the fly

        // keep in mind, the policy is cached for 5 minutes by default (TTL is configurable in the authorizer)
        // and will apply to subsequent calls to any method/resource in the RestApi
        // made with the same token

        // the example policy below denies access to all resources in the RestApi
        return new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage));
	}

}

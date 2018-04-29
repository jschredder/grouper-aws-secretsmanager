package edu.internet2.middleware.grouperClient.config;

import java.nio.ByteBuffer;
import java.io.IOException;
//these imports could be more specific
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsAsyncClientBuilder;
import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
//these imports could be more specific
import com.amazonaws.services.secretsmanager.*;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
//these imports could be more specific
import com.amazonaws.services.secretsmanager.model.*;

/**
 * EL class for getting secrets from AWS SecretsManager
 * @author jschrad4
 *
 */
public class SecretsManagerElClass {

  /**
   * method for EL
   * @param secretName
   * @param secretValue
   * @return the result
   */
  public static String getSecret(String secretName, String secretValue) {

    String endpoint = "secretsmanager.us-east-1.amazonaws.com";
//can we get this dynamically or should this be a grouper variable
    String region = "us-east-1";

    AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
    clientBuilder.setEndpointConfiguration(config);
    AWSSecretsManager client = clientBuilder.build();

    String secret;
    ByteBuffer binarySecretData;

    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest();
    getSecretValueRequest.withSecretId(secretName);

    GetSecretValueResult getSecretValueResponse = null;


//need to fix the logging here

    try {
        getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

    } catch(ResourceNotFoundException e) {
        System.out.println("The requested secret " + secretName + " was not found");
    } catch (InvalidRequestException e) {
        System.out.println("The request was invalid due to: " + e.getMessage());
    } catch (InvalidParameterException e) {
        System.out.println("The request had invalid params: " + e.getMessage());
    }

    if(getSecretValueResponse == null) {
        return null;
    }

    // Decrypted secret using the associated KMS CMK
    // Depending on whether the secret was a string or binary, one of these fields will be populated
    if(getSecretValueResponse.getSecretString() != null) {
        secret = getSecretValueResponse.getSecretString();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(secret);
            return jsonNode.get(secretValue).asText();
        }
//This should be a jackson exception
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    else {
        return null;
    }
    return null;
  }
}

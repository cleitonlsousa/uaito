package com.uaito.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DependencyFactory {

    @PostConstruct
    public void postConstruct(){

        AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider("administrator"))
                .withRegion(Regions.US_EAST_1)
                .build();

        //.withCredentials(new ProfileCredentialsProvider("administrator"))
                //.build();

        GetParameterRequest request = new GetParameterRequest();
        request.setName("parameter_name");
        request.setWithDecryption(true);
        GetParameterResult result = ssm.getParameter(request);

        System.out.println(result.getParameter().getValue());

    }

}

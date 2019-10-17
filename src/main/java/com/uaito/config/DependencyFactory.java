package com.uaito.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.uaito.exception.UaiToException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class DependencyFactory {

    private String dbURL = "db_url";

    private String dbUser = "db_user";

    private String dbPass = "db_pass";

    @PostConstruct
    public void postConstruct() throws UaiToException {

        YamlConfig yamlConfig = getYaml();

        dbURL = yamlConfig.getVar().get(dbURL);
        dbUser = yamlConfig.getVar().get(dbUser);
        dbPass = yamlConfig.getVar().get(dbPass);

        AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider("administrator"))
                .withRegion(Regions.US_EAST_1)
                .build();

        GetParametersResult parameters = ssm.getParameters(new GetParametersRequest()
                .withNames(dbURL,dbUser,dbPass)
                .withWithDecryption(true));
        Map<String, Object> config = new HashMap<>();
        parameters.getParameters().forEach(parameter -> {
            config.put(parameter.getName(), parameter.getValue());
        });

        dbURL = (String) config.get(dbURL);
        dbUser = (String) config.get(dbUser);
        dbPass = (String) config.get(dbPass);

    }

    private YamlConfig getYaml() throws UaiToException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(
                    ResourceUtils.getFile("classpath:application.yml"), YamlConfig.class);

        } catch (Exception e) {
            throw new UaiToException(e.getMessage());
        }
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getDbURL() {
        return dbURL;
    }
}

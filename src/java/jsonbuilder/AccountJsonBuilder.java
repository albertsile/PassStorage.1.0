/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilder;

import entity.Account;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author artjo
 */
public class AccountJsonBuilder {
    public JsonObject createJsonAccount(Account account){
        UserJsonBuilder userJsonBuilder = new UserJsonBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", account.getId());
        job.add("resourceUrl", account.getResourceUrl());
        job.add("login", account.getLogin());
        job.add("password",account.getPassword());
        job.add("registrationDate",account.getRegistrationDate().toString());
        job.add("user", userJsonBuilder.cerateJsonUser(account.getUser()));
        
        return job.build();
    }
}

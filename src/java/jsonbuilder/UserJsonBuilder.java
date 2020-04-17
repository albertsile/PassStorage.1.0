/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilder;

import entity.User;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author artjo
 */
public class UserJsonBuilder {
    public JsonObject cerateJsonUser(User user){
        PersonJsonBuilder personJsonBuilder = new PersonJsonBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", user.getId());
        job.add("login", user.getLogin());
        job.add("active", user.isActive());
        job.add("person", personJsonBuilder.createJsonPerson(user.getPerson()));
        return job.build();
    }
}

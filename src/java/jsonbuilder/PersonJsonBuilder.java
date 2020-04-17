/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilder;

import entity.Person;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author artjo
 */
public class PersonJsonBuilder {
    public JsonObject createJsonPerson(Person person){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", person.getId());
        job.add("firstname", person.getFirstname());
        job.add("lastname", person.getLastname());
        job.add("email",person.getEmail());
        return job.build();
    }
}

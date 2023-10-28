package com.developedbysaurabh.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role {

    @Id
    private String roleId;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

/*
@JsonIgnore
It is associated with libraries like Jackson or JSON serialization/deserialization in the context of RESTful web services. However, it's often used in Spring-based applications when working with JSON.

Here's what @JsonIgnore does:

JsonIgnore during Serialization: When you use @JsonIgnore on a field or a getter method in your entity class, it instructs the Jackson library (used by Spring for JSON processing) to exclude that specific field from being serialized into JSON when you return the object as a response in a REST API. This is useful when you have fields that you don't want to expose to the client or fields that should not be included in the JSON response.

JsonIgnore during Deserialization: Similarly, if you apply @JsonIgnore to a setter method, it tells Jackson to ignore that setter during deserialization. This means that when Jackson is parsing JSON data and converting it into a Java object, it won't try to set the value of the field associated with the ignored setter.

*/
}

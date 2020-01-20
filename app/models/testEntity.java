package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;

//import play.data.validation.*;
//import play.db.ebean.Model;
import io.ebean.Model;
//import play.db.jpa.Model;

//@Entity
public class testEntity<E,C> /*extends Model*/ {

    @Id
    public Long id;
    private List<E> links;

    public List<E> getLinks() {
        return this.links;
    }

    public void addLink(E link) {
        this.links.add(link);
    }
}
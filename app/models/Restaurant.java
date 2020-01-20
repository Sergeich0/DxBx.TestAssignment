package models;

import io.ebean.Finder;
import io.ebean.Model;
import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.Constraint;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Restaurant extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Column(unique=true)
    private String INN;
    private String name;
    private String ULName;
    private String address;
    private boolean active;

    public static Finder<Long, Restaurant> find = new Finder(Restaurant.class);

    @ManyToMany(mappedBy="restaurants")
    private List<Guest> guests;


    @Inject
    public Restaurant(Long id, String INN, String name, String ULName, String address, boolean active)
    {

        this.id = id;
        setAddress(address);
        setINN(INN);
        setName(name);
        setULName(ULName);
        setActive(active);
    }

    public void update(Restaurant rest)
    {
        if (!rest.getId().equals(this.id)) {
            throw new Error("Unexpected error with different IDs of restaurants");
        }

        String newAddress = rest.getAddress();
        setAddress(newAddress);

        String newINN = rest.getINN();
        setINN(newINN);

        String newName = rest.getName();
        setName(newName);

        String newULName = rest.getULName();
        setULName(newULName);

        boolean newActive = rest.isActive();
        setActive(newActive);
        this.update();
    }

    public Long getId()
    {
        return id;
    }

    public void setId() {
        throw new Error("Id can't be setted");
    }

    public String getINN()
    {

        return this.INN;
    }

    public void setINN(String newINN)
    {
        newINN = newINN.trim();
        Pattern p_inn = Pattern.compile("^\\d{10}(\\d{2}|)$");
        Matcher m_inn = p_inn.matcher(newINN);
        if (!m_inn.find())
        {
            throw new Error("INN must contain only 10 or 12 digits");
        }
        this.INN = newINN;
    }
    public String getName()
    {
        return this.name;
    }

    public void setName(String newName)
    {
        if (newName.length() <= 50)
        {
            this.name = newName;
        }
        else
        {
            throw new Error("Too long restaurant name: 50 symbols only");
        }
    }
    public String getULName()
    {
        return this.ULName;
    }

    public void setULName(String newULName)
    {
        if (newULName.length() <= 50)
        {
            this.ULName = newULName;
        }
        else
        {
            throw new Error("Too long restaurant UL name: 50 symbols only");
        }
    }
    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String newAddress)
    {
        if (newAddress.length() <= 200)
        {
            this.address = newAddress;
        }
        else
        {
            throw new Error("Too long restaurant adress: 200 symbols only");
        }
    }

    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public List<Guest> getGuests()
    {
        return this.guests;
    }

    public void addGuest(String gotted)
    {
        try {
            @Constraints.Email String email = gotted;
        } catch (Error e) {
            e.printStackTrace();
        }
        Guest guest = Guest.find.query().where().or()
                .like("phone", gotted)
                .like("email", gotted)
                .endOr().findOne();
        if (guest == null) {
            throw new Error("There are no guest with phone or email " + gotted);
        }
        if (this.guests.contains(guest)) {
            throw new Error("There are already exist guest with phone or email " + gotted);
        }
        if (!guest.isActive()) {
            throw new Error("Guest must be active");
        }
        this.guests.add(guest);
        this.update();
    }

    public void deleteGuest(String deleted)
    {
        Long id = Long.parseLong(deleted);
        this.guests.removeIf(nextGuest -> nextGuest.getId().equals(id));
        this.update();
    }
}

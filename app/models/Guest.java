package models;

import io.ebean.Finder;
import io.ebean.Model;
import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.Constraint;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Guest extends Model {

    @Id
    public Long id;

    @Constraints.Required
    private String firstName;
    private String lastName;
    @Constraints.Required
    @Column(unique=true)
    private String phone;
    @Constraints.Required
    @Constraints.Email
    @Column(unique=true)
    private String email;
    private boolean active = true;

    public static Finder<Long, Guest> find = new Finder(Guest.class);
    @ManyToMany
    private List<Restaurant> restaurants;


    @Inject
    public Guest(Long id, String firstName, String lastName, String phone, String email, boolean active) {

        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setEmail(email);
        setActive(active);
    }

    public Long getId() {
        //throw new Error("id:" + this.id);
        return id;
    }

    public void setId() {
        // don't do it
        throw new Error("Id can't be setted");
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public void setFirstName(String f_name)
    {
        f_name = f_name.trim();
        Pattern p_f_name = Pattern.compile("^[А-ЯЁЙ][а-яёй]+$");
        Matcher m_f_name = p_f_name.matcher(f_name);
        if (!m_f_name.find())
        {
            throw new Error("First Name must be a one word with first uppercase letter and lowercase other letters");
        }
        this.firstName = f_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String l_name)
    {
        l_name = l_name.trim();
        Pattern p_l_name = Pattern.compile("^([А-ЯЁЙ][-а-яёй]+)$");
        Matcher m_l_name = p_l_name.matcher(l_name);
        if (!m_l_name.find())
        {
            throw new Error("Last Name must be a one word with first uppercase letter and lowercase other letters (or double last name with \"-\")");
        }
        this.lastName = l_name;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String new_phone)
    {
        new_phone = new_phone.trim();
        Pattern p_new_phone = Pattern.compile("^(\\s|\\n|\\t|\\b)((8|\\+?7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?([\\d\\- ]{9,11})$");
        Matcher m_new_phone = p_new_phone.matcher(new_phone);
        if (!m_new_phone.find())
        {
            throw new Error("Phone number must be a phone number");
        }
        // String phone_number = m_new_phone.group();
        Pattern p_phone_number = Pattern.compile("\\d");
        Matcher m_phone_number = p_phone_number.matcher(new_phone);
        String numbers_of_phone = "";
        while (m_phone_number.find()) {
            numbers_of_phone += m_phone_number.group();
        }
        if (numbers_of_phone.length() == 10)
        {
            numbers_of_phone = "7" + numbers_of_phone;
        }
        else if (numbers_of_phone.startsWith("8") && numbers_of_phone.length() == 11)
        {
            numbers_of_phone = numbers_of_phone.replaceFirst("8", "7");
        }
        else if (numbers_of_phone.startsWith("7") && numbers_of_phone.length() == 11)
        {
            // nothing to do
        }
        else
        {
            throw new Error("Strange phone number");
        }
        this.phone = numbers_of_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String new_email) {
        this.email = new_email;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public  List<Restaurant> getRestaurants()
    {
        return this.restaurants;
    }

    public void update(Guest guest) {
        if (!guest.getId().equals(this.id)) {
            throw new Error("Unexpected error with different IDs of guest");
        }

        String newFirstName = guest.getFirstName();
        setFirstName(newFirstName);

        String newLastName = guest.getLastName();
        setLastName(newLastName);

        String newPhone = guest.getPhone();
        setPhone(newPhone);

        String newEmail = guest.getEmail();
        setEmail(newEmail);

        boolean newActive = guest.isActive();
        setActive(newActive);

        this.update();
    }

    public void addRestaurant(String gotted)
    {

        String INN = gotted;
        Restaurant restaurant = Restaurant.find.query().where().and()
                .like("INN", INN)
                .findOne();
        if (restaurant == null) {
            throw new Error("There are no restaurant with INN " + INN);
        }
        if (this.restaurants.contains(restaurant)) {
            throw new Error("There are already exist restaurant with INN " + INN);
        }
        if (!restaurant.isActive()) {
            throw new Error("Restaurant must be active");
        }
        this.restaurants.add(restaurant);
        this.update();
    }

    public void deleteRestaurant(String deleted)
    {
        Long id = Long.parseLong(deleted);
        this.restaurants.removeIf(nextRestaurant -> nextRestaurant.getId().equals(id));
        this.update();
    }
}

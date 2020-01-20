package controllers;
import io.ebean.DuplicateKeyException;
import models.*;

import play.data.Form;
import play.mvc.*;
import play.data.FormFactory;

import javax.inject.Inject;
import java.util.Map;

import static io.ebean.Expr.eq;

public class RestaurantController extends Controller {

    @Inject private FormFactory formFactory;
    public Result restaurants() {

//        Restaurant rest1 = new Restaurant(1L, "1029384756", "Rest", "OOO Rest", "ulitsa Pushkina, dom Kolotushkina");
//        Restaurant rest2 = new Restaurant(2L,"1029384657", "Rerest", "OOO Rerest", "ulitsa Pushkina, dom Kokololotushkina");
//        List<Restaurant> rests = new ArrayList<Restaurant>();
//        rests.add(rest1);
//        rests.add(rest2);

        return ok(views.html.restaurants.render(Restaurant.find.all(), formFactory.form(Restaurant.class)));
    }

    public Result restaurant(Long id) {

//        Restaurant rest1 = new Restaurant(1L, "1029384756", "Rest", "OOO Rest", "ulitsa Pushkina, dom Kolotushkina");
//        Restaurant rest2 = new Restaurant(2L,"1029384657", "Rerest", "OOO Rerest", "ulitsa Pushkina, dom Kokololotushkina");
//        List<Restaurant> rests = new ArrayList<Restaurant>();
//        rests.add(rest1);
//        rests.add(rest2);

        return ok(views.html.restaurant.render(Restaurant.find.byId(id), formFactory.form(Restaurant.class), id));
    }

    public Result addrest() {
        Form<Restaurant> filledForm = formFactory.form(Restaurant.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            //return ok(filledForm.errors().toString());
            return badRequest(views.html.restaurants.render(Restaurant.find.all(), filledForm));
        }
        Restaurant restaurant = filledForm.get();
        //return ok(restaurant.toString());
        try {
            restaurant.save();
        } catch (DuplicateKeyException e) {
            return badRequest(views.html.restaurants.render(Restaurant.find.all(), filledForm.withError("Address", e.getMessage())));
        }
        return redirect(routes.RestaurantController.restaurants());
    }

    public Result editrest(Long restId)
    {

        Form<Restaurant> filledForm = formFactory.form(Restaurant.class).bindFromRequest();

        if (filledForm.hasErrors()) {
            //return ok(filledForm.errors().toString());
            return badRequest(views.html.restaurant.render(Restaurant.find.byId(restId), filledForm, restId));
        }
        Restaurant restaurant = Restaurant.find.byId(restId);
        //return ok(restaurant.toString());
        if (restaurant == null) {
            return badRequest(views.html.restaurants.render(Restaurant.find.all(), formFactory.form(Restaurant.class)));
        }
        Restaurant newRestaurant = filledForm.get();
        restaurant.update(newRestaurant);
        return redirect(routes.RestaurantController.restaurant(restId));
    }

    public Result editGuest(Long restId, Http.Request request)
    {
        String gotted = request.getQueryString("searchable");
        String deleted = request.getQueryString("deleted");
//        return ok("gotted " + gotted + " deleted " + deleted);
        Restaurant restaurant = Restaurant.find.byId(restId);
        if (gotted != null)
        {
            try {
                restaurant.addGuest(gotted);
            } catch (Error e) {
                return badRequest(views.html.restaurant.render(Restaurant.find.byId(restId), formFactory.form(Restaurant.class).withError("Address", e.getMessage()), restId));
            }
        }
        else if (deleted != null)
        {
//            try {
                restaurant.deleteGuest(deleted);
//              } catch (NumberFormatException e) {
//                return badRequest(views.html.restaurant.render(Restaurant.find.byId(restId), formFactory.form(Restaurant.class).withError("Address", e.getMessage()), restId));
//            }
        }
        else
        {
            return ok("ne ok");
        }

        return redirect(routes.RestaurantController.restaurant(restId));
    }
}

package controllers;

import io.ebean.DuplicateKeyException;
import models.Guest;
import models.Restaurant;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class GuestController extends Controller {

    @Inject private FormFactory formFactory;
    public Result guests() {

//        Restaurant rest1 = new Restaurant(1L, "1029384756", "Rest", "OOO Rest", "ulitsa Pushkina, dom Kolotushkina");
//        Restaurant rest2 = new Restaurant(2L,"1029384657", "Rerest", "OOO Rerest", "ulitsa Pushkina, dom Kokololotushkina");
//        List<Restaurant> rests = new ArrayList<Restaurant>();
//        rests.add(rest1);
//        rests.add(rest2);

        return ok(views.html.guests.render(Guest.find.all(), formFactory.form(Guest.class)));
    }

    public Result addguest()
    {
        Form<Guest> filledForm = formFactory.form(Guest.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            //return ok(filledForm.errors().toString());
            return badRequest(views.html.guests.render(Guest.find.all(), filledForm));
        }
        Guest guest = filledForm.get();
        try {
            guest.save();
        } catch (DuplicateKeyException e)
        {
            String key = "";
            String error = e.getMessage();
            if (error.contains("uq_guest_phone")) {
                key = "Phone";
            }
            if (error.contains("uq_guest_email")) {
                key = "Email";
            }
            return badRequest(views.html.guests.render(Guest.find.all(), filledForm.withError(key, e.getMessage())));
        }
        return redirect(routes.GuestController.guests());
    }

    public Result guest(Long id)
    {
        return ok(views.html.guest.render(Guest.find.byId(id), formFactory.form(Guest.class), id));
    }

    public Result editGuest(Long guestId, Http.Request request)
    {

        Form<Guest> filledForm = formFactory.form(Guest.class).bindFromRequest(request);

        if (filledForm.hasErrors()) {
            return badRequest(views.html.guest.render(Guest.find.byId(guestId), filledForm, guestId));
        }
        Guest guest = Guest.find.byId(guestId);
        //return ok(restaurant.toString());
        if (guest == null) {
            return badRequest(views.html.restaurants.render(Restaurant.find.all(), formFactory.form(Restaurant.class)));
        }
        Guest newGuest = filledForm.get();
        guest.update(newGuest);
        return redirect(routes.GuestController.guest(guestId));
    }


    public Result editRest(Long guestId, Http.Request request)
    {
        String gotted = request.getQueryString("searchable");
        String deleted = request.getQueryString("deleted");
//        return ok("gotted " + gotted + " deleted " + deleted);
        Guest guest = Guest.find.byId(guestId);
        if (gotted != null)
        {
            try {
                guest.addRestaurant(gotted);
            } catch (Error e) {
                return badRequest(views.html.guest.render(Guest.find.byId(guestId), formFactory.form(Guest.class).withError("searchable", e.getMessage()), guestId));
            }
        }
        else if (deleted != null)
        {
            try {
            guest.deleteRestaurant(deleted);
              } catch (NumberFormatException e) {
                return badRequest(views.html.guest.render(Guest.find.byId(guestId), formFactory.form(Guest.class).withError("searchable", e.getMessage()), guestId));
            }
        }
        else
        {
            return ok("ne ok");
        }

        return redirect(routes.GuestController.guest(guestId));
    }

}

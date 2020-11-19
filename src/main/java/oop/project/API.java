package oop.project;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
class API {

    @GetMapping("/schedule/{date}")
    List<Vessel> findbyDate(@PathVariable String date) {
        return Database.findByDate(date);
    }

    @PostMapping("/register")
    boolean register(@RequestParam String user, String pass) {
        return Database.addUser(user, pass);
    }

    @PostMapping("/login")
    boolean login(@RequestParam String user, String pass) {
        return Database.loginUser(user, pass);
    }

    @PostMapping("/resetpwd")
    boolean resetPassword(@RequestParam String user) {
        return Database.resetPassword(user);
    }

    @PostMapping("/changepwd")
    boolean changePassword(@RequestParam String user, String oldpass, String newpass) {
        return Database.changePassword(user, oldpass, newpass, false);
    }

    @GetMapping("/favourites")
    List<Vessel> getFavouriteList(@RequestParam String user) {
        return Database.getFavouriteList(user);
    }

    @PostMapping("/addfav")
    boolean addFavourite(@RequestParam String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        return Database.addFavourite(user, abbrVslM, inVoyN, shiftSeqN);
    }

    @PostMapping("/delfav")
    boolean deleteFavourite(@RequestParam String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        return Database.deleteFavourite(user, abbrVslM, inVoyN, shiftSeqN);
    }

    @GetMapping("/subscriptions")
    List<Vessel> getSubscriptionList(@RequestParam String user) {
        return Database.getSubscriptionList(user);
    }

    @PostMapping("/addsub")
    boolean addSubscription(@RequestParam String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        return Database.addSubscription(user, abbrVslM, inVoyN, shiftSeqN);
    }

    @PostMapping("/delsub")
    boolean deleteSubscription(@RequestParam String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        return Database.deleteSubscription(user, abbrVslM, inVoyN, shiftSeqN);
    }

}
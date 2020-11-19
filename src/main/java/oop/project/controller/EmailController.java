package oop.project.controller;

import oop.project.Vessel;
import oop.project.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {

    private static EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        EmailController.emailService = emailService;
    }

    public static void sendChangeNotif(String user, Vessel vessel) {
        String subject = "Change in " + vessel.getAbbrVslM() + " Arrival Timing";
        String body = "Arrival timing for " + vessel.getAbbrVslM() + " (" + vessel.getInVoyN() + " / "
                + vessel.getOutVoyN() + ") is now " + vessel.getBthgDt() + ".";

        emailService.sendMail(user, subject, body);
    }

    public static void sendPwdReset(String user, String pass) {
        String subject = "Password Reset";
        String body = "The temporary password for " + user + " is " + pass
                + " . Please login and change it as soon as possible.";

        emailService.sendMail(user, subject, body);
    }
}
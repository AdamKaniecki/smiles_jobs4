package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.RoleRepository;

@Controller
@AllArgsConstructor
@SessionAttributes("username")
public class LoginController {
    private UserService userService;
//    private RoleRepository roleRepository;
//    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/loginUser")
    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {

        User user = userService.findByUserName(username);

        if (user != null && user.getPassword().equals(password) && user.getUserName().equals(username)) {
            if (hasCandidateRole(user)) {
                session.setAttribute("user", user); // Przechowaj użytkownika w sesji
                System.out.println("Zalogowano pomyślnie");
                model.addAttribute("username", username);
                return "candidate_portal";
            } else {
                System.out.println("Użytkownik nie posiada wymaganej roli.");
                model.addAttribute("error", "Użytkownik nie posiada wymaganej roli.");
                return "login";
            }
        } else {
            System.out.println("Nieprawidłowe dane logowania.");
            model.addAttribute("error", "Nieprawidłowe dane logowania.");
            return "login";
        }
    }

    private boolean hasCandidateRole(User user) {
        for (RoleEntity role : user.getRoles()) {
            if (role.getRole().equals("ROLE_CANDIDATE")) {
                return true;
            }
        }
        return false;
    }
}


//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }



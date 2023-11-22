package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.business.CvService;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "{user}/company_portal";
    public static final String CREATE_JOB_OFFER = "/create_job_offer";
    private HttpSession httpSession;
    private JobOfferService jobOfferService;
    private UserService userService;
    private UserMapper userMapper;
    private CvService cvService;


    @GetMapping(COMPANY_PORTAL)
    public String getCompanyPortalPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Użytkownik jest zalogowany
            model.addAttribute("user", user);

            List<CV> cvList = cvService.findAll();
            model.addAttribute("cvList", cvList);
            return "company_portal";
        } else {
            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
            return "redirect:/login";
        }
    }

    @GetMapping(CREATE_JOB_OFFER)
    public String createJobOfferForm(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "create_job_offer";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }


    @PostMapping("/createJobOffer")
    public String createdJobOffers(
            @ModelAttribute("jobOffer") JobOffer jobOffer,
            Model model) {
        log.info("Received job offer: {}", jobOffer);
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
//            jobOffer.setDateTime(OffsetDateTime.now());
            jobOffer.setUser(loggedInUser);
            jobOfferService.create(jobOffer, loggedInUser);

            // Dodaj reklamę do modelu, aby przekazać ją do widoku
            model.addAttribute("jobOffer", jobOffer);
            model.addAttribute("user", loggedInUser);

            return "job_offer_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }

    }


@GetMapping("/search")
public String searchAdvertisements(
        @RequestParam("keyword") String keyword,
        @RequestParam("category") String category,
        Model model) {
    List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
    model.addAttribute("searchResults", searchResults);
    model.addAttribute("keyword", keyword);
    model.addAttribute("category", category);
    return "search_results";
}

    @GetMapping("/search_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(category,keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_results";
    }


}

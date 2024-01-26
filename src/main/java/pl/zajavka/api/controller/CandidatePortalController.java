

package pl.zajavka.api.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.NotificationMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.*;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

//@SessionAttributes("userSession")
@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
    public static final String USER_ID = "/show";

    private HttpSession httpSession;
    private UserService userService;
    private UserRepository userRepository;
    private UserMapperDTO userMapperDTO;
    private JobOfferService jobOfferService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
    private CvService cvService;
    private EntityManager entityManager;
    private SortingService sortingService;
    private PaginationService paginationService;

    @SneakyThrows
    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(
            Model model,
            Authentication authentication,
            HttpSession httpSession,
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User loggedInUser = userService.getLoggedInUser(authentication);
        httpSession.setAttribute("user", loggedInUser);

        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);

        // Paginacja dla ofert pracy
        Page<JobOfferDTO> jobOfferDTOPage = jobOfferService.findAll(pageable)
                .map(jobOfferMapperDTO::map);

        model.addAttribute("jobOfferDTOs", jobOfferDTOPage.getContent());
        model.addAttribute("currentPage", jobOfferDTOPage.getNumber()) ; // Page numbers start from 1
        model.addAttribute("totalPages", jobOfferDTOPage.getTotalPages());
        model.addAttribute("totalItems", jobOfferDTOPage.getTotalElements());

        // Sortowanie dla ofert pracy
//        sortingService.sortJobOfferById();

        int previousPage = Math.max(jobOfferDTOPage.getNumber(),0);
        int nextPage = Math.min(jobOfferDTOPage.getNumber() + 2, jobOfferDTOPage.getTotalPages());

        model.addAttribute("previousPage", previousPage + 1);
        model.addAttribute("nextPage", nextPage);

        return "candidate_portal";
    }



    //        model.addAttribute("jobOffersDTOs", jobOfferDTOPage.getContent());
//      model.addAttribute("jobOffersDTOs", jobOfferDTOPage.getContent());
//        model.addAttribute("jobOffersDTOs", jobOfferDTOPage);
//        model.addAttribute("currentPage", jobOfferDTOPage.getNumber() + 1); // Page numbers start from 1
//        model.addAttribute("totalPages", jobOfferDTOPage.getTotalPages());
//        model.addAttribute("totalItems", jobOfferDTOPage.getTotalElements());

//        Page<NotificationDTO> notificationDTOPage = notificationService.findByUser(loggedInUser, pageable)
//                .map(notificationMapperDTO::map);
//        model.addAttribute("notifications", notificationDTOPage.getContent());



    @GetMapping("/searchJobOffers")
    public String searchJobOffers(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_job_offers_results"; // Twój widok do wyświetlania wyników wyszukiwania ofert pracy
    }

    @GetMapping("/search_job_offers_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(category, keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_job_offers_results";
    }


    @PostMapping("/changeMeetingDate")
    public String changeMeetingDate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();

        notificationService.changeMeetingDate(notification, loggedInUser, adresat);

        return "cv_created_successfully";
    }


    @PostMapping("/acceptMeetingDate")
    public String acceptNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);

        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();

        notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

        return "cv_created_successfully";
    }


    @PostMapping("/sendCV")
    @Transactional
    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication, HttpSession httpSession) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        CV cv = cvService.findByUser2(loggedInUser);
        User adresat = jobOffer.getUser();

        if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {
            // Dodaj odpowiedni komunikat lub przekierowanie w przypadku powtórnego wysłania CV
            return "cv_already_sent";
        }

        Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
        userService.save(loggedInUser);
        userService.save(adresat);

        return "cv_created_successfully";
    }




}

















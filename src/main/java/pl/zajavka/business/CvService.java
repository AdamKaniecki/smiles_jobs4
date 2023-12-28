package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.AddressRepository;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
@Slf4j

@Service
@AllArgsConstructor
public class CvService {

    private CvMapper cvMapper;
    private CvRepository cvRepository;
    private UserMapper userMapper;
    private AddressMapper addressMapper;
    private AddressRepository addressRepository;
    private AddressService addressService;


    @Transactional
    public CV createCV(CV cv, User user) {
        // Sprawdź, czy użytkownik już ma CV
        if (cvRepository.existsByUser(userMapper.map(user))) {
            // Obsłuż przypadki, gdy użytkownik już ma CV
            // Możesz rzucić odpowiednim wyjątkiem lub zwrócić odpowiedni wynik
            // W tym przykładzie zwracam null, ale możesz dostosować to do swoich potrzeb
            return null;
        }
        Address addressCV = cv.getAddress();

        CvEntity newEntity = CvEntity.builder()
                .id(cv.getId())
                .name(cv.getName())
                .surname(cv.getSurname())
                .dateOfBirth(cv.getDateOfBirth())
                .sex(cv.getSex())
                .maritalStatus(cv.getMaritalStatus())
                .contactEmail(cv.getContactEmail())
                .phoneNumber(cv.getPhoneNumber())
                .education(cv.getEducation())
                .workExperience(cv.getWorkExperience())
                .skills(cv.getSkills())
                .language(cv.getLanguage())
                .languageLevel(cv.getLanguageLevel())
                .hobby(cv.getHobby())
                .user(userMapper.map(user))
                .address(addressMapper.map(addressCV))
                .build();

        cvRepository.saveAndFlush(newEntity);
        return cvMapper.map(newEntity);
    }


    public List<CV> findAll() {
        return cvRepository.findAll().stream()
                .map(cvMapper::map)
                .toList();
    }

    public List<CV> searchCvByKeywordAndCategory(String keyword, String category) {
        List<CvEntity> searchResultEntities = cvRepository.findCvByKeywordAndCategory(keyword, category);
        return searchResultEntities.stream()
                .map(cvMapper::map)
                .toList();
    }

    public Optional<CV> getCVById(Integer cvId) {
        return cvRepository.findById(cvId).map(cvMapper::map);
    }
//    public Optional<CV> findCvByUserId(Integer id) {
//        return cvRepository.findByUserId(id);
//    }

    public boolean existByUser(User loggedInUser) {
        return cvRepository.existsByUser(userMapper.map(loggedInUser));
    }

    public Optional<CV> findById(Integer id) {
        log.debug("szukaj id w serwisie: w ", id);
        return cvRepository.findById(id).map(cvMapper::map);
    }

    public Optional<CV> findByUser(User user) {
        Optional<CvEntity> cvEntityOptional = cvRepository.findByUser(userMapper.map(user));
        return cvEntityOptional.map(cvMapper::map);
    }


    @Transactional
    public CV updateCV(CV updatedCv) {
        if (updatedCv.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            CvEntity cvEntity = cvRepository.findById(updatedCv.getId())
                    .orElseThrow(() -> new NotFoundException("CV with ID " + updatedCv.getId() + " not found"));

            // Aktualizuj pola CV na podstawie danych z formularza
            cvEntity.setName(updatedCv.getName());
            cvEntity.setSurname(updatedCv.getSurname());
            cvEntity.setDateOfBirth(updatedCv.getDateOfBirth());
            cvEntity.setSex(updatedCv.getSex());
            cvEntity.setMaritalStatus(updatedCv.getMaritalStatus());
            cvEntity.setContactEmail(updatedCv.getContactEmail());
            cvEntity.setPhoneNumber(updatedCv.getPhoneNumber());
            cvEntity.setEducation(updatedCv.getEducation());
            cvEntity.setWorkExperience(updatedCv.getWorkExperience());
            cvEntity.setSkills(updatedCv.getSkills());
            cvEntity.setLanguage(updatedCv.getLanguage());
            cvEntity.setLanguageLevel(updatedCv.getLanguageLevel());
            cvEntity.setHobby(updatedCv.getHobby());

//            // Aktualizuj dane adresowe
//            AddressEntity addressEntity = cvEntity.getAddress();
//            if (addressEntity == null) {
//                // Utwórz nowy obiekt adresu, jeśli nie istnieje
//                addressEntity = new AddressEntity();
//                cvEntity.setAddress(addressEntity);
//            }
//
//            // Ustaw nowe wartości adresu i zaktualizuj go przy użyciu metody z AddressService
//            addressEntity.setCountry(updatedCv.getAddress().getCountry());
//            addressEntity.setCity(updatedCv.getAddress().getCity());
//            addressEntity.setStreetAndNumber(updatedCv.getAddress().getStreetAndNumber());

            // Zapisz zaktualizowany obiekt CV w bazie danych
            CvEntity cvEntityUpdate = cvRepository.save(cvEntity);

            return cvMapper.map(cvEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new NotFoundException("CV ID cannot be null");
        }
    }



        }




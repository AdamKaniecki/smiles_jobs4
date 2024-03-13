package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {



    List<NotificationEntity> findBySenderUser(UserEntity user);
    List<NotificationEntity> findByReceiverUser(UserEntity user);
    @Query("SELECT n FROM NotificationEntity n WHERE n.senderUser = :user OR n.receiverUser = :user")
    List<NotificationEntity> findByUser(UserEntity user);

    void deleteByCvId(Integer cvId);


    List<NotificationEntity> findByCvId(Integer id);

    boolean existsBySenderUserAndJobOffer(UserEntity userEntity, JobOfferEntity jobOfferEntityOffer);

    Page<NotificationEntity> findAll(Pageable pageable);

    List<NotificationEntity> findByJobOfferId(Integer id);


//    Page<NotificationEntity> findByUser(UserEntity userEntity, Pageable pageable);
}

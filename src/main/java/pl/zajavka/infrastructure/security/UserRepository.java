package pl.zajavka.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.CvEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String userName);


    boolean existsByEmail(String email);

//    @Query("SELECT u FROM UserEntity u WHERE u.cv = :cvEntity")
//    Optional<UserEntity> findByCV(@Param("cvEntity") CvEntity cvEntity);
}

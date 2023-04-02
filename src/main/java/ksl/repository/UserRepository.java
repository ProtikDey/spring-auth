package ksl.repository;

import ksl.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);

    List<UserEntity> findAllByuserName(String userName);

    List<UserEntity> findAllByemail(String email);
}

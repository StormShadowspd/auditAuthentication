package com.audit.authentication.repository;

import com.audit.authentication.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<UserRecord, String> {
    UserRecord findByUserName(String userName);
}

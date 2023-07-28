package org.Stasy.PublicPrivacyAppBackendHeroku.repository;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface OpinionsRepository extends JpaRepository<Opinion, Long> {

        Opinion findOpinionById(int id);
        List<Opinion> findOpinionByCollaboratorName(String username);

        String findUsernameById(Long id);//return String==>because we are returning name!

        String findCollaboratorNameById(int id);

        void deleteById(int id);
}
package org.Stasy.PublicPrivacyAppBackendHeroku.service;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserAlreadyExistsException;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserServiceInterface extends JpaRepository<User, Long>{

    //save 是JpaRepository自己的方法
//    public User save(User user) {
//        return userRepository.save(user);
//    }
    //this is called JPQL

    //User findByUsername(String username) throws UserAlreadyExistsException, UserNotFoundException;
    User findUserByUsername(String username) throws UserAlreadyExistsException, UserNotFoundException;

    User findUserByEmail(String email)throws UserNotFoundException;

    void delete(User entity);

}
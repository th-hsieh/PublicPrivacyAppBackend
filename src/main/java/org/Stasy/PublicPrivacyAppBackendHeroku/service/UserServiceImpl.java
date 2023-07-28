package org.Stasy.PublicPrivacyAppBackendHeroku.service;

import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.Stasy.PublicPrivacyAppBackendHeroku.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserServiceImpl implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserServiceImpl(){};

    //method
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user.equals (null)) {
            throw new UserNotFoundException() ;
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {//throws UserNotFoundException
        User user = userRepository.findByEmail(email);
        if (user.equals (null)) {
            throw new UserNotFoundException() ;
        }
        return user;
    }

    @Override
    public void deleteById(Long Id) {

    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }




    ////VERY IMPORTANT
    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    public void deleteByUsername(String username) {
        User user=userRepository.findByUsername(username);
        userRepository.delete(user);
    }

    ////VERY IMPORTANT
    @Override
    public User getOne(Long aLong) {
        return null;
    }

    @Override
    public User getById(Long aLong) {
        return null;
    }

    @Override
    public User getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }



    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }


}

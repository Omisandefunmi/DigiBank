package com.semicolon.DigiBank.data.repositories.user;

import com.semicolon.DigiBank.data.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private Map<String, User> userRepo = new HashMap<>();


    @Override
    public User saveUser(User user) {
        String accountName = user.getFirstName()+ " "+ user.getLastName();
         userRepo.put(accountName, user);
        return user;
    }

    @Override
    public User findUserByAccountName(String accountName) {
        return userRepo.get(accountName);
    }


}

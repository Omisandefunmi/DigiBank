package com.semicolon.DigiBank.services.bank;


import com.semicolon.DigiBank.data.models.User;
import com.semicolon.DigiBank.data.repositories.bank.BankRepository;
import com.semicolon.DigiBank.dtos.requests.DeleteAccountRequest;
import com.semicolon.DigiBank.services.account.AccountService;
import com.semicolon.DigiBank.web.exceptions.AccountNotFoundException;
import com.semicolon.DigiBank.web.exceptions.InvalidDetailsException;
import com.semicolon.DigiBank.web.exceptions.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final AccountService accountService;

//    public BankServiceImpl(BankRepository bankRepository) {
//        this.bankRepository = bankRepository;
//    }

    @Override
    public User saveCustomer(User savedUser) {
        return bankRepository.saveCustomer(savedUser);
    }

    @Override
    public String deleteUser(DeleteAccountRequest request) throws InvalidDetailsException, AccountNotFoundException, UserDoesNotExistException {
        User user = bankRepository.findUserByAccountNumber(request.getAccountNumber());
        if(user == null){
            throw new UserDoesNotExistException("User does not exist");
        }
        return accountService.deleteAccount(request);
    }


}

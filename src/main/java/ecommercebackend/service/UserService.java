package ecommercebackend.service;

import ecommercebackend.api.model.RegistationBody;
import ecommercebackend.exception.UserAlreadyExistsException;
import ecommercebackend.model.LocalUser;
import ecommercebackend.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
    }

    public LocalUser registerUser(RegistationBody registationBody) throws UserAlreadyExistsException {
        if (localUserDAO.findByUsernameIgnoreCase(registationBody.getUsername()).isPresent()
            || localUserDAO.findByEmailIgnoreCase(registationBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registationBody.getEmail());
        user.setFirstName(registationBody.getFirstName());
        user.setLastName(registationBody.getLastName());
        user.setUsername(registationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registationBody.getPassword()));

        return localUserDAO.save(user);
    }
}

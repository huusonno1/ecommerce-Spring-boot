package ecommercebackend.service;

import ecommercebackend.api.model.LoginBody;
import ecommercebackend.api.model.RegistationBody;
import ecommercebackend.exception.UserAlreadyExistsException;
import ecommercebackend.model.LocalUser;
import ecommercebackend.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;



    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
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

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if(opUser.isPresent()){
            LocalUser user = opUser.get();
            if(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}

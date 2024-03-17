package ecommercebackend.api.controller.auth;

import ecommercebackend.api.model.LoginBody;
import ecommercebackend.api.model.LoginResponse;
import ecommercebackend.api.model.RegistationBody;
import ecommercebackend.exception.UserAlreadyExistsException;
import ecommercebackend.model.LocalUser;
import ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistationBody registationBody)  {
        try {
            userService.registerUser(registationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = userService.loginUser(loginBody);
        if(jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }
}

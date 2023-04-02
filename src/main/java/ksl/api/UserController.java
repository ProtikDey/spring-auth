package ksl.api;


import jakarta.validation.Valid;
import ksl.model.request.UserRegistrationRequest;
import ksl.model.response.UserRegistrationResponse;
import ksl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    private UserRegistrationResponse registerUser(@Valid @RequestBody UserRegistrationRequest request){
        log.info("Received user registration request: {}", request );
        return userService.registerUser(request);
    }

}

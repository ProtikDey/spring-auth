package ksl.service;

import ksl.model.request.UserRegistrationRequest;
import ksl.model.response.UserRegistrationResponse;

public interface UserService {

    UserRegistrationResponse registerUser(UserRegistrationRequest request);
}

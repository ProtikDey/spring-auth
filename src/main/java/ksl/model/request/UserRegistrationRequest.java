package ksl.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    private String email;


    @NotNull
    @NotBlank
    private String password;

    @NotNull
    private Long roleId;


}

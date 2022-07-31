package Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class AuthTokenRequest {

    private String username;
    private String password;

}

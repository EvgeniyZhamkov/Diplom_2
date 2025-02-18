package client;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Client {
    private String email;
    private String password;
    private String name;

    public Client( String password, String name) {
        this.password = password;
        this.name = name;
    }
}

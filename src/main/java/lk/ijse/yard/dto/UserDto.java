package lk.ijse.yard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserDto {

    private String userId;
    private String name;
    private String email;
    private String password;

    public UserDto(String userId, String password){
         this.userId = userId;
         this.password = password;
    }

}

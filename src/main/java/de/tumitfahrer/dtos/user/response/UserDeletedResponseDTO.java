package de.tumitfahrer.dtos.user.response;


import de.tumitfahrer.dtos.http.ResponseDTO;

public class UserDeletedResponseDTO extends ResponseDTO {
    private UserDeletedDTO user;

    public UserDeletedDTO getUser() {
        return user;
    }

    public void setUser(UserDeletedDTO user) {
        this.user = user;
    }
}

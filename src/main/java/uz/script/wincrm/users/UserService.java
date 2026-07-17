package uz.script.wincrm.users;

import java.util.List;

public interface UserService {
    UserResponse create(UserDTO dto);
    UserResponse findById(Long id);
    List<UserResponse> fetchAllUsers();
    UserResponse update(Long id, UserDTO dto);
    void delete(Long id);
    void activeOrDisabledUser(Long id);
    UserStatResponse getUserStats(Long userId);
}

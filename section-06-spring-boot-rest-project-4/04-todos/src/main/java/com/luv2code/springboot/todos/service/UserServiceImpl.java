package com.luv2code.springboot.todos.service;

import com.luv2code.springboot.todos.entity.User;
import com.luv2code.springboot.todos.repository.UserRepository;
import com.luv2code.springboot.todos.request.PasswordUpdateRequest;
import com.luv2code.springboot.todos.response.UserResponse;
import com.luv2code.springboot.todos.util.FindAuthenticatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo() {
        return UserResponse.from(findAuthenticatedUser.getAuthenticatedUser());
    }

    @Override
    public void deleteUser() {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        if (isLastAdmin(user)) {
            throw new ResponseStatusException(FORBIDDEN, "Admin cannot delete itself");
        }
        userRepository.delete(user);
    }

    private boolean isLastAdmin(User user) {
        if (!user.isAdmin()) {
            return false;
        }

        long adminCount = userRepository.countAdminUsers();
        return adminCount <= 1;
    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
        User user = findAuthenticatedUser.getAuthenticatedUser();

        if (!isOldPasswordCorrect(user.getPassword(), passwordUpdateRequest.getOldPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Current password is incorrect");
        }

        if (!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(),
                                    passwordUpdateRequest.getNewPassword2())) {
            throw new ResponseStatusException(BAD_REQUEST, "New passwords do not match");
        }

        if (!isNewPasswordDifferent(passwordUpdateRequest.getOldPassword(),
                                    passwordUpdateRequest.getNewPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Old and new passwords must be different");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        userRepository.save(user);
    }

    private boolean isOldPasswordCorrect(String currentPassword, String oldPassword) {
        return passwordEncoder.matches(oldPassword, currentPassword);
    }

    private boolean isNewPasswordConfirmed(String newPassword, String newPasswordConfirmation) {
        return newPassword.equals(newPasswordConfirmation);
    }

    private boolean isNewPasswordDifferent(String oldPassword, String newPassword) {
        return !oldPassword.equals(newPassword);
    }
}


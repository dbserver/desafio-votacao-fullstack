package db.server.app.domain.User.Controllers;

import db.server.app.commons.App.UseCases.AuthUserUseCase;
import db.server.app.commons.Records.Http.HttpErrorResponse;
import db.server.app.domain.User.DTO.AuthResponse;
import db.server.app.domain.User.DTO.LoginCommand;
import db.server.app.domain.User.DTO.LoginDTO;
import db.server.app.domain.User.DTO.UserDTO;
import db.server.app.domain.User.Model.User;
import db.server.app.domain.User.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthUserUseCase authUserUseCase;

    public UserController(UserService userService,
                          AuthUserUseCase authUserUseCase) {
        this.userService = userService;
        this.authUserUseCase = authUserUseCase;
    }

    @GetMapping("/health")
    public String Health() {
        return this.userService.getHealth();
    }

    @PostMapping
    public ResponseEntity NewUser(
            @RequestBody UserDTO userDTO
    ) throws Exception {
        Optional<User> currentUser = this.userService.getUserByEmail(userDTO.email());
        if (!currentUser.isEmpty()) {
            Optional<String> message = Optional.of("This email is already registered");
            return ResponseEntity.
                    status(HttpStatus.NOT_ACCEPTABLE).
                    body(new HttpErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), message));
        }

        UserDTO response = this.userService.save(userDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity Login(
            @RequestBody LoginDTO loginDTO
    ) throws Exception {
        LoginCommand command = new LoginCommand(loginDTO.email());

        try {
            AuthResponse response = this.authUserUseCase.execute(command);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(new HttpErrorResponse(HttpStatus.UNAUTHORIZED.value(), Optional.of(e.getMessage())));
        }

    }
}

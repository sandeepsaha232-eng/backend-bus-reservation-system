package in.BusReservation.Userservice.controller;

import in.BusReservation.Userservice.DTO.*;
import in.BusReservation.Userservice.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    // 1. Check whether user already exists
    // 2. Validate user information
    // 3. Create user
    // 4. Update user
    // 5. Find user
    // 6. Delete/deactivate user
    private  final UserService userService;
    UserController(UserService userService) {
        this.userService = userService;
    }
    //check if user exist or not
    @GetMapping("/exist/{email}")
    public ResponseEntity<Boolean> exist(@PathVariable String email){
         return ResponseEntity.ok(userService.checkUser(email));
    }
    //validate
    @PostMapping("/validate")
    public ResponseEntity<UserResponse> validateUser(@RequestBody UserValidateRequest request){
       UserResponse userResponse = userService.validUser(request);
       return ResponseEntity.ok(userResponse);
    }
    //Create User
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        UserResponse userResponse= userService.createUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    //Update user
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody  UserUpdateRequest request){
       UserResponse userResponse = userService.updateUser(request);
        return  ResponseEntity.ok(userResponse);
    }
   // email for check
    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email){
        UserResponse userResponse =userService.search(email);
        return ResponseEntity.ok(userResponse);
    }
    //delete
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteRequest request){
        userService.deleteUser(request);
        return ResponseEntity.ok().build();
    }
}

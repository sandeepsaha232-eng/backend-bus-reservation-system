package in.BusReservation.Userservice.Service;

import in.BusReservation.Userservice.DTO.*;
import in.BusReservation.Userservice.Entity.User;
import in.BusReservation.Userservice.Exception.*;
import in.BusReservation.Userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


// 1. Check whether user already exists
// 2. Validate user information
// 3. Create user
// 4. Update user
// 5. Find user
// 6. Delete/deactivate user
@Service
public class UserService {
    private final UserRepository userRepository;
    private  final  PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //1.
    public boolean checkUser(String email ){
       return userRepository.existsById(email);
    }
    //2.
    public UserResponse validUser(UserValidateRequest user){
       String email =  user.getEmail();
       String password = user.getPassword();
       User user1 = userRepository.findById(email).orElseThrow(()-> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(password,user1.getPassword())){
            if(user1.getName().equals(user.getName())){
                if(user1.getPhone_no()==user.getPhone_no()){
                    UserResponse userResponse = new UserResponse();
                    userResponse.setName(user1.getName());
                    userResponse.setEmail(user1.getEmail());
                    userResponse.setPhone_no(user1.getPhone_no());
                    return userResponse;
                }else {
                    throw new PhoneNoIncorrectException("phone number doesn't match");
                }
            }else {
                throw new NameIncorrectException("Name doesnt match");
            }
        }else{
            throw  new PasswordIncorrectException("Password is wrong");
        }
    }
    //3.
    public UserResponse createUser(UserCreateRequest user1){
        if (userRepository.existsById(user1.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists");
        }

        String encodedPassword = passwordEncoder.encode(user1.getPassword());
        User user  = new User(user1.getEmail(),user1.getName(),encodedPassword,user1.getPhone_no());
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setPhone_no(user.getPhone_no());
        System.out.println("User created successfully");
        return userResponse;
    }
    //4.UPDATE
    @Transactional
    public UserResponse updateUser(UserUpdateRequest user1){
        String email = user1.getEmail();
        User user = userRepository.findById(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(user1.getCurrPassword(),user.getPassword())){
            throw  new PasswordIncorrectException("Passwords do not match");
        }
        String encoderPassword=passwordEncoder.encode(user1.getChangePassword());
        user.setName(user1.getName());
        user.setPassword(encoderPassword);
        user.setPhone_no(user1.getPhone_no());
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setPhone_no(user.getPhone_no());
        return  userResponse;
    }
    //5. find user
    public UserResponse search(String email ){
       User user =  userRepository.findById(email).orElseThrow(()->new UserNotFoundException("User not found"));
       UserResponse userResponse = new UserResponse();
       userResponse.setName(user.getName());
       userResponse.setEmail(user.getEmail());
       userResponse.setPhone_no(user.getPhone_no());
       return userResponse;
    }
    //6.
    public void deleteUser(UserDeleteRequest user){
        String email = user.getEmail();
        User user1 = userRepository.findById(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
        if(passwordEncoder.matches(user.getPassword(),user1.getPassword())){
            userRepository.delete(user1);
        }
        else {
            throw  new PasswordIncorrectException("Incorrect Passowrd ");
        }
    }
}

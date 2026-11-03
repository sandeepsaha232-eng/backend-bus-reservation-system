package in.BusReservation.Userservice.DTO;

public class UserCreateRequest {
    String email ;
    String password;
    String name;
    int phone_no ;

    public UserCreateRequest(String email, String name, String password, int phone_no) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(int phone_no) {
        this.phone_no = phone_no;
    }
}

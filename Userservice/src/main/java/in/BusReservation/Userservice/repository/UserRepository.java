package in.BusReservation.Userservice.repository;

import in.BusReservation.Userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {}

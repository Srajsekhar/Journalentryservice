package net.engineeringdigest.journalApp.repositry;
import net.engineeringdigest.journalApp.entity.Journalentry;
import net.engineeringdigest.journalApp.entity.Userentry;
import org.apache.catalina.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserentryRepo extends MongoRepository<Userentry, String>{
    Userentry findByName(String name);
    Userentry deleteByname(String name);
}

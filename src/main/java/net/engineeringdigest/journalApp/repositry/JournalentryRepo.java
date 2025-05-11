package net.engineeringdigest.journalApp.repositry;
import net.engineeringdigest.journalApp.entity.Journalentry;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface JournalentryRepo extends MongoRepository<Journalentry, String>{
}

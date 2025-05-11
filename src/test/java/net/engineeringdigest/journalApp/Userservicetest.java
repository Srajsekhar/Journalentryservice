package net.engineeringdigest.journalApp;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.repositry.UserentryRepo;
import net.engineeringdigest.journalApp.service.UserEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class Userservicetest {

    @Autowired
    private  UserEntryService userEntryService;
    @Autowired
   private UserentryRepo userentryRepo;
    @Test
    public void userentrytest(){
        Userentry userentry = userEntryService.findbyname("vishnu");
        assertNotNull(userentry);
    }
}

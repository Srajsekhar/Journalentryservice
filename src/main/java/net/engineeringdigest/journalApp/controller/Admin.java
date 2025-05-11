package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Journalentry;
import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Component
@RestController
@RequestMapping("/admin")

public class Admin {
    @Autowired
    private UserEntryService userEntryService;
    @GetMapping("/all-users")
    public ResponseEntity<?> getAll() {
        List<Userentry> all = userEntryService.getAll();
        if(!all.isEmpty() && all!=null) return new ResponseEntity<>(all, HttpStatus.OK);
        else{
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/create-admin")
    public  boolean provideAdminAccess(@RequestBody  Userentry user){
        Userentry saveadmin = userEntryService.saveadmin(user);
        if(saveadmin!=null){
            return  true;
        }
        return  false;
    }
}

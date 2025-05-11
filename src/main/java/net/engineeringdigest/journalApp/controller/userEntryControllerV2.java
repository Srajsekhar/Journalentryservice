package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.service.UserEntryService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class userEntryControllerV2 {

    @Autowired
    private UserEntryService userEntryService;
    @GetMapping
    public ResponseEntity<?> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        Userentry findbyname = userEntryService.findbyname(user);
        return new ResponseEntity<>(findbyname, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> findbyname(@RequestBody Userentry userentry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        Userentry findbyname = userEntryService.findbyname(user);
            findbyname.setName(userentry.getName());
            findbyname.setPassword(userentry.getPassword());
            userEntryService.saveUserEntry(findbyname);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping
    public Boolean deleteusername(@RequestBody Userentry userentry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String name=authentication.getName();
        userEntryService.deleteByname(name);
        return true;
    }
}

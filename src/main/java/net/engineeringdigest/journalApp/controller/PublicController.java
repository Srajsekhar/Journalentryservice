package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/public")
@RestController
public class PublicController {
    @Autowired
    UserEntryService userEntryService;
    @PostMapping
    public Boolean createusers(@RequestBody Userentry userentry){
        userEntryService.saveUserEntry(userentry);
        return true;
    }
}

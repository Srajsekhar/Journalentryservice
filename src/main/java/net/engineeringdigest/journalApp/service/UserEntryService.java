package net.engineeringdigest.journalApp.service;
import java.util.Arrays;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.repositry.UserentryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Service
public class UserEntryService {
    private static final Logger logger = LoggerFactory.getLogger(UserEntryService.class);
    @Autowired
    private UserentryRepo userentryRepo;
    public static final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    public void saveUserEntry(Userentry user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(asList("USERS"));
            userentryRepo.save(user);
        } catch (Exception e){
            logger.error("error occur for {}", user.getName(),e);
        }
    }
    public void saveEntry(Userentry user) {
        userentryRepo.save(user);
        logger.info("saved the user entries");
    }
    public List<Userentry> getAll(){
        if (logger.isInfoEnabled()) {
            logger.info("finding all user entries");
        }
        return userentryRepo.findAll();

    }
    public Optional<Userentry> findById(String id){
        logger.info("finding the userntry of {}", id);
        return userentryRepo.findById(id);
    }
    public void deleteById(String id){
        userentryRepo.deleteById(id);
        logger.info("user entry of {} is deleted",id);
    }
    public List<Userentry> sortById(){
       return userentryRepo.findAll(Sort.by(Sort.Direction.ASC, "_id"));
    }
    public Userentry findbyname(String name){
        logger.info("finding the user entries by {}",name);
       return userentryRepo.findByName(name);
    }
    public  Userentry deleteByname(String name){
        logger.info("user entry is deleted by {}",name);
        return  userentryRepo.deleteByname(name);
    }

    public Userentry saveadmin(Userentry user) {
        user.setRoles(asList("USERS", "ADMIN"));
        logger.info("user {} is set as ADMIN ", user.getName() );
        return userentryRepo.save(user);
    }
}

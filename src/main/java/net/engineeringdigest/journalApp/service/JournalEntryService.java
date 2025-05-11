package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Journalentry;
import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.repositry.JournalentryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalentryRepo journalentryRepo;
    @Autowired
    private  UserEntryService userEntryService;
    @Transactional
    public void saveJournalEntry(Journalentry journalentry, String name) {
        try {
            System.out.println("Before saving journal entry: " + journalentry);
            Userentry userentry = userEntryService.findbyname(name);
            journalentry.setDate(LocalDateTime.now());
            Journalentry save = journalentryRepo.save(journalentry);
            System.out.println(save.getTitle());
            userentry.getJournalentries().add(save);
            userEntryService.saveEntry(userentry);
        }catch (Exception e){
//            System.out.println(e);
            log.error("error occurred while saving the journal entry", e);
//            throw new RuntimeException("something went wrong"+e);
        }
    }
    public void saveJournalEntry(Journalentry journalentry) {
        journalentry.setDate(LocalDateTime.now());
         journalentryRepo.save(journalentry);
         log.info("saved journal entries");
        //Userentry userentry = userEntryService.findbyname(name);
        //userentry.getJournalentries().add(save);
        //userEntryService.saveUserEntry(userentry);
    }
    public List<Journalentry> getAll(){
        return journalentryRepo.findAll();
    }
    public Optional<Journalentry> findById(String id){
        return journalentryRepo.findById(id);
    }
    @Transactional
    public Boolean deleteById(String id, String name){
       try {
           Userentry userentry = userEntryService.findbyname(name);
           journalentryRepo.deleteById(id);
           boolean removeIf = userentry.getJournalentries().removeIf(x -> x.getId().equals(id));
           if(removeIf){
               userEntryService.saveEntry(userentry);
               return  true;
           }else {
              return false;
           }
       }catch (Exception e){
//           System.out.println(e);
           log.error("error occurred while deleting an entry of {}",name,e);
           throw new RuntimeException("error occurred while deleting an entry", e);
       }
    }
    public List<Journalentry> sortById(){
       return journalentryRepo.findAll(Sort.by(Sort.Direction.ASC, "_id"));
    }
}

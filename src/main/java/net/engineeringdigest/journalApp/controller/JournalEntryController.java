package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Journalentry;
import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.externalapi.WeatherResponse;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.RedisService;
import net.engineeringdigest.journalApp.service.UserEntryService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/journal")
public class
JournalEntryControllerV2 {
    private WeatherService weatherService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserEntryService userEntryService;
    @GetMapping
    public  ResponseEntity<Map<String, Object>>  getAllUserEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        Userentry findbyname = userEntryService.findbyname(user);
        if (findbyname == null) {
            return ResponseEntity.notFound().build();
        }
        WeatherResponse temperature = redisService.getvaluefromredis("Weather_city");

        try {
            WeatherResponse currentWeather = weatherService.getCurrentWeather("Mumbai");
            redisService.savetoredis("Weather_city", currentWeather);
            temperature = currentWeather;
        }
        catch (Exception e){
            System.out.println("Error fetching weather data: " + e.getMessage());
        }
        List<Journalentry> entries = findbyname.getJournalentries();
        Map<String, Object> respone= new HashMap<>();
        respone.put("username", entries);
        respone.put("temperature",temperature.getCurrent().getTemperature());
        return new ResponseEntity<>(respone, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Journalentry> createEntry(@RequestBody Journalentry entity) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user=authentication.getName();
            Userentry findbyname = userEntryService.findbyname(user);
            journalEntryService.saveJournalEntry(entity, user);
            return new ResponseEntity<>(entity, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Journalentry> getJournalEntryById(@PathVariable String id) {
        Optional<Journalentry> entris = journalEntryService.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        Userentry userentry = userEntryService.findbyname(user);
        List<Journalentry> journalentryStream = userentry.getJournalentries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!journalentryStream.isEmpty()){
            if (entris.isPresent()) {
                return new ResponseEntity<>(entris.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Journalentry> updateJournalById(@PathVariable String id, @RequestBody Journalentry updatedEntry) {
        Journalentry optional = journalEntryService.findById(id).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Userentry userentry = userEntryService.findbyname(user);
        List<Journalentry> journalentryStream = userentry.getJournalentries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!journalentryStream.isEmpty()) {
            if (optional != null) {
                optional.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().equals("") ? updatedEntry.getTitle() : optional.getTitle());
                optional.setName(updatedEntry.getName() != null && !updatedEntry.getName().equals("") ? updatedEntry.getName() : optional.getName());
                journalEntryService.saveJournalEntry(optional);
                return new ResponseEntity<>(optional, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Boolean removed = journalEntryService.deleteById(id, user);
        //List<Journalentry> entries = journalEntryService.getAll();
        if(removed){
            return new ResponseEntity<>(journalEntryService.getAll(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(journalEntryService.getAll(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/sorted")
    public  ResponseEntity<?> getAllSortedById(){
      return new ResponseEntity<>(journalEntryService.sortById(),HttpStatus.OK) ;
    }
}

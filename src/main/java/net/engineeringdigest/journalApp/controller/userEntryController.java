package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.externalapi.WeatherResponse;
import net.engineeringdigest.journalApp.service.RedisService;
import net.engineeringdigest.journalApp.service.UserEntryService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userEntryControllerV2 {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private UserEntryService userEntryService;
    @Autowired
    private RedisService redisService;
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user=authentication.getName();
        Userentry findbyname = userEntryService.findbyname(user);
        if(findbyname==null){
            return ResponseEntity.notFound().build();
        }
        WeatherResponse temperature = redisService.getvaluefromredis("Weather_city");
        if (temperature == null) {
            WeatherResponse currentWeather = weatherService.getCurrentWeather("Mumbai");
            redisService.savetoredis("Weather_city", currentWeather);
            temperature= currentWeather;
        }
        int temperature1 = temperature.getCurrent().getTemperature();
        Map<String, Object> response = new HashMap<>();
        response.put("userEntry", findbyname);
        response.put("temperature", temperature1);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

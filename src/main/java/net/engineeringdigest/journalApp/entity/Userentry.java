package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class Userentry {
    @Indexed(unique = true)
    @NonNull
    private  String name;
    @Id
    private  String id;
    @NonNull
    private  String password;
    @DBRef
    private List<Journalentry> journalentries= new ArrayList<>();
    private  List<String> roles;


}

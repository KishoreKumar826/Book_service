package com.example.library_BookMs.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.library_BookMs.Domain.book;
import com.example.library_BookMs.Repository.Repo;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class BookController {
    @Autowired
    private Repo repo;
    @Autowired
    private KafkaTemplate<String,String> kafka ;

    @GetMapping("/")
    public String getMethodName() {
        return "Welcome to Book service";
    }

    @GetMapping("/sentData/{message}")
    public String publish(@PathVariable String message){
        kafka.send("BookCopies", message, message);
        System.out.println(" message sent "+message);
        return "Message sent!";
    }

    @GetMapping("/fetchAllBooks")
    public List<book> fetchAllBooks(){
        return repo.findAll();

        
    }
    
    
    @GetMapping("/findByISBN/{isbn}")
    public ResponseEntity<book> findByISBN(@PathVariable Integer isbn){
         book found=repo.findById(isbn).get();
         if(found==null){
          return  ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok().body(found);

    }

    @DeleteMapping("/deleteBookByISBN/{isbn}")
    public ResponseEntity<Object> deleteBookByISBN(@PathVariable Integer isbn){
       book found=repo.findById(isbn).get();
       if(null==found){
        return ResponseEntity.notFound().build();
       }
       repo.deleteById(isbn);
       return ResponseEntity.ok().build();
    }

    @PostMapping("/addNewBook")
    //client from web browser sent data in json format. @requestbody used to convert it to java object
    
    public ResponseEntity<Object> addNewBook(@RequestBody book bookobj){
        repo.save(bookobj);
        return ResponseEntity.ok(bookobj);

        
    }
    

}

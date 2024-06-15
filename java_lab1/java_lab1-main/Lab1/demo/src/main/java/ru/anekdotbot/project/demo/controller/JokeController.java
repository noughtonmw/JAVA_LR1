package ru.anekdotbot.project.demo.controller;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.anekdotbot.project.demo.model.JokeData;
import ru.anekdotbot.project.demo.repository.JokeRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/jokes")
public class JokeController {

    @Autowired
    private JokeRepository jokeRepository;

    @GetMapping
    public List<JokeData> getAllJokes() {
        return jokeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JokeData> getJokeById(@PathVariable int id) {
        Optional<JokeData> joke = jokeRepository.findById(id);
        return joke.map(j -> new ResponseEntity<>(j, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<JokeData> addJoke(@RequestBody JokeData joke) {
        JokeData savedJoke = jokeRepository.save(joke);
        return new ResponseEntity<>(savedJoke, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JokeData> updateJoke(@PathVariable int id, @RequestBody JokeData updatedJoke) {
        if (jokeRepository.existsById(id)) {
            updatedJoke.setId(id);
            JokeData savedJoke = jokeRepository.save(updatedJoke);
            return new ResponseEntity<>(savedJoke, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoke(@PathVariable int id) {
        if (jokeRepository.existsById(id)) {
            jokeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

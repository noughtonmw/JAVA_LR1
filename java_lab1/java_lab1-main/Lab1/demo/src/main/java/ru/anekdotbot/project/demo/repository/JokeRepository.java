package ru.anekdotbot.project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.anekdotbot.project.demo.model.JokeData;

public interface JokeRepository extends JpaRepository<JokeData, Integer>{
    
}


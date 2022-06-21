package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skeleton.hibernate.HibernateSession;
import skeleton.model.GameConfig;
import skeleton.persistence.IGameConfigRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/gameConfig")
public class SkeletonGameConfigController {
    @Autowired
    private HibernateSession hibernateSession;


    @Autowired
    private IGameConfigRepository gameConfigRepository;

    @RequestMapping(method = RequestMethod.POST)
    public GameConfig create(@RequestBody GameConfig gameConfig) {
        System.out.println("Saving game data...");
        gameConfigRepository.setSessionFactory(hibernateSession.sessionFactory);
        gameConfig = gameConfigRepository.add(gameConfig);
        return gameConfig;
    }
}

package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skeleton.hibernate.HibernateSession;
import skeleton.model.GameData;
import skeleton.persistence.IGameDataRepository;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/gameData")
public class SkeletonGameDataController {

    @Autowired
    private HibernateSession hibernateSession;

    @Autowired
    private IGameDataRepository gameDataRepository;

    @RequestMapping(value = "/{id_joc}", method = RequestMethod.GET)
    public GameData[] getGameData(@PathVariable String id_joc){
        System.out.println("Get game data for game");
        System.out.println(id_joc);
        gameDataRepository.setSessionFactory(hibernateSession.sessionFactory);
        List<GameData> gameDataList = (List<GameData>) gameDataRepository.findAllJoc(Integer.valueOf(id_joc));
        return gameDataList.toArray(new GameData[gameDataList.size()]);
    }

    @RequestMapping(method = RequestMethod.POST)
    public GameData create(@RequestBody GameData gameData) {
        System.out.println("Saving game data...");
        gameDataRepository.setSessionFactory(hibernateSession.sessionFactory);
        gameData = gameDataRepository.add(gameData);
        return gameData;
    }
}
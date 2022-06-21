package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skeleton.hibernate.HibernateSession;
import skeleton.model.GameFinish;
import skeleton.persistence.IGameFinishRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/gameFinish")
public class SkeletonGameFinishController {
    @Autowired
    private HibernateSession hibernateSession;

    @Autowired
    private IGameFinishRepository gameFinishRepository;

    @RequestMapping(value = "/{id_jucator}", method = RequestMethod.GET)
    public GameFinish[] getGameData(@PathVariable String id_jucator){
        System.out.println("game finish data for player");
        System.out.println(id_jucator);
        gameFinishRepository.setSessionFactory(hibernateSession.sessionFactory);
        List<GameFinish> gameFinishList = (List<GameFinish>) gameFinishRepository.findAllPlayer(id_jucator);
        return gameFinishList.toArray(new GameFinish[gameFinishList.size()]);
    }
}

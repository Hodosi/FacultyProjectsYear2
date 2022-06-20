package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skeleton.hibernate.HibernateSession;
import skeleton.model.Move;
import skeleton.persistence.IMoveRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/move")
public class SkeletonMoveController {

    @Autowired
    private HibernateSession hibernateSession;

    @Autowired
    private IMoveRepository moveRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Move[] getAll(){
        System.out.println("Get all tests...");
        moveRepository.setSessionFactory(hibernateSession.sessionFactory);
        List<Move> movesList = (List<Move>) moveRepository.findAll();
        return movesList.toArray(new Move[movesList.size()]);
    }
}
package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import skeleton.hibernate.HibernateSession;
import skeleton.model.Move;
import skeleton.persistence.IHistoryRepository;
import skeleton.persistence.IMoveRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/moves")
public class SkeletonGameController {

    @Autowired
    private HibernateSession hibernateSession;

//    @Autowired
//    private IHistoryRepository historyRepository;

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

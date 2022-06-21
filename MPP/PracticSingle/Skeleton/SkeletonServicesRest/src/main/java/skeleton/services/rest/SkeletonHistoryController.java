package skeleton.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import skeleton.hibernate.HibernateSession;
import skeleton.model.Clasament;
import skeleton.model.History;
import skeleton.model.Move;
import skeleton.persistence.IHistoryRepository;
import skeleton.persistence.IMoveRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/skeleton/history")
public class SkeletonHistoryController {

    @Autowired
    private HibernateSession hibernateSession;

    @Autowired
    private IHistoryRepository historyRepository;


//    @RequestMapping(value = "/{id_joc}/{id_jucator}", method = RequestMethod.GET)
//    public History[] getAllByJocJucator(@PathVariable String id_joc, @PathVariable String id_jucator){
//        System.out.println("Get all histories...");
//        System.out.println(id_joc);
//        System.out.println(id_jucator);
//        historyRepository.setSessionFactory(hibernateSession.sessionFactory);
//        List<History> historyList = (List<History>) historyRepository.findAllJocJucator(Integer.valueOf(id_joc), id_jucator);
//        return historyList.toArray(new History[historyList.size()]);
//    }

    @RequestMapping(value = "/{id_joc}/{id_runda}", method = RequestMethod.GET)
    public History[] getAllByJocRunda(@PathVariable String id_joc, @PathVariable String id_runda){
        System.out.println("Get all histories...");
        System.out.println(id_joc);
        System.out.println(id_runda);
        historyRepository.setSessionFactory(hibernateSession.sessionFactory);
        List<History> historyList = (List<History>) historyRepository.findAllJocRunda(Integer.valueOf(id_joc), Integer.valueOf(id_runda));
        return historyList.toArray(new History[historyList.size()]);
    }


//    @RequestMapping(value = "/{id_joc}", method = RequestMethod.GET)
//    public History[] getClasament(@PathVariable String id_joc){
//        System.out.println("Get clasament...");
//        System.out.println(id_joc);
//        historyRepository.setSessionFactory(hibernateSession.sessionFactory);
//        List<History> historyList = (List<History>) historyRepository.findClasament(Integer.valueOf(id_joc));
//        return historyList.toArray(new History[historyList.size()]);
//    }

    @RequestMapping(value = "/{id_joc}", method = RequestMethod.GET)
    public Clasament[] getClasament(@PathVariable String id_joc){
        System.out.println("Get clasament...");
        System.out.println(id_joc);
        historyRepository.setSessionFactory(hibernateSession.sessionFactory);
        List<Clasament> clasamentList = (List<Clasament>) historyRepository.findClasament(Integer.valueOf(id_joc));
        return clasamentList.toArray(new Clasament[clasamentList.size()]);
    }
}

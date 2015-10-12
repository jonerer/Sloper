package se.lolcalhost.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.lolcalhost.models.Item;
import se.lolcalhost.models.User;
import se.lolcalhost.models.Vote;
import se.lolcalhost.repositories.ItemRepository;
import se.lolcalhost.repositories.UserRepository;
import se.lolcalhost.repositories.VoteRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jonmar on 2015-10-12.
 * https://dzone.com/articles/slope-one-recommender
 */
@Controller
public class IndexController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    public static <E> List<E> makeCollection(Iterable<E> iter) {
        List<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

//    public float[][] buildDiffMatrix() {
//        List<User> users = makeCollection(userRepository.findAll());
//        List<Item> items = makeCollection(itemRepository.findAll());
//
//        float[][] mteste = new float[items.size()][items.size()];
//        int[][] mFreq = new int[items.size()][items.size()];
//
//        for(int i = 1; i <= items.size(); i++)
//            for(int j = 1; j <= items.size(); j++){
//                mteste[i][j] = 0;
//                mFreq[i][j] = 0;
//            }
//
//        for (User user : users) {
//            List<Vote> votes = user.getVotes();
//            for (int i = 0; i < votes.size(); i++) {
//                Vote item1 = votes.get(i);
//                for (int j = 0; j < votes.size(); j++) {
//                    Vote item2 = votes.get(j);
//
//                    mteste[i][j] = mteste[i][j] + (item1.value - item2.value);
//                    mFreq[i][j] = mFreq[i][j] + 1;
//                }
//            }
//        }
//
//                /*  Calculate the averages (diff/freqs) */
//        for(int i = 1; i<= items.size(); i++){
//            for(int j = i; j <= items.size(); j++){
//                if(mFreq[i][j] > 0){
//                    mteste[i][j] = mteste[i][j] / mFreq[i][j];
//                }
//            }
//        }
//
////        /* Iterate through all users, and then, through all items do calculate the diffs */
////        for(int cUser : usersMatrix.keySet()){
////            for(int i: usersMatrix.get(cUser).keySet()){
////                for(int j : usersMatrix.get(cUser).keySet() ){
////                    mteste[i][j] = mteste[i][j]  +
////                            ( usersMatrix.get(cUser).get(i).floatValue() - (usersMatrix.get(cUser).get(j).floatValue()));
////                    mFreq[i][j] = mFreq[i][j] + 1;
////                }
////            }
////        }
//
////        /*  Calculate the averages (diff/freqs) */
////        for(int i = 1; i<= maxItemsId; i++){
////            for(int j = i; j <= maxItemsId; j++){
////                if(mFreq[i][j] > 0){
////                    mteste[i][j] = mteste[i][j] / mFreq[i][j];
////                }
////            }
////        }
//    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public String create_item(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        Item item = new Item();
        item.name = name;
        itemRepository.save(item);
        model.addAttribute("text", "hej");
        return "redirect:/";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String create_user(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        User user = new User();
        user.name = name;
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    public String create_vote(HttpServletRequest request, Model model) {
        Long userId = Long.parseLong(request.getParameter("user"));
        Long itemId = Long.parseLong(request.getParameter("item"));
        User user = userRepository.findOne(userId);
        Item item = itemRepository.findOne(itemId);
        float voteValue = Float.parseFloat(request.getParameter("vote"));

        Vote vote = new Vote(user, item, voteValue);
        voteRepository.save(vote);

        return "redirect:/";
    }

    @RequestMapping("/")
    public String index(Model model) {
        Iterable<User> users = userRepository.findAll();
        Iterable<Item> items = itemRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("items", items);

        Iterable<User> users2 = userRepository.findAll();
        Iterator<User> iterator = users2.iterator();
        if (iterator.hasNext()) {
            User next = iterator.next();
            System.out.println(next);
            List<Vote> votes = next.getVotes();
            for (Vote vote : votes) {
                System.out.println(vote);
            }
        }

        return "index";
    }
}

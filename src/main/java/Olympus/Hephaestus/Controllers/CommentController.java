package Olympus.Hephaestus.Controllers;

import Olympus.Hephaestus.DAO.CommentService;
import Olympus.Hephaestus.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/comments")
@RestController
@CrossOrigin
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService c){commentService=c;}

    @GetMapping
    public List<Comment> getAllComments(){return commentService.list();}

    @GetMapping(path="{id}")
    public Optional<Comment> getCommentById(@PathVariable int id){return commentService.get(id);}

    @GetMapping(path="/bypost/{postID}")
    public List<Comment> getAllCommentsByPost(@PathVariable int postID){return commentService.listByPost(postID);}

    @PostMapping
    public int writeComment(@RequestBody Comment c){return commentService.create(c);}

    @PutMapping(path="{id}")
    public void editComment(@RequestBody Comment c, @PathVariable int id){
        commentService.update(c,id);
    }

    @DeleteMapping(path="{id}")
    public void deleteComment(@PathVariable int id){commentService.delete(id);}


}

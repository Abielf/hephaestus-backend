package Olympus.Hephaestus.Controllers;

import Olympus.Hephaestus.DAO.PostService;
import Olympus.Hephaestus.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/posts")
@RestController
@CrossOrigin
public class PostController {
    PostService postService;

    @Autowired
    public PostController(PostService p){postService=p;}

    @GetMapping
    public List<Post> getAllPosts(){return postService.list();}

    @GetMapping(path="{id}")
    public Optional<Post> getPostById(@PathVariable int id){return postService.get(id);}

    @GetMapping(path="/bytag/{tag}")
    public List<Post> getPostByTag(@PathVariable String tag){return postService.listByTag(tag);}

    @PostMapping
    public int writeBlog(@RequestBody Post p){return postService.create(p);}

    @PutMapping(path="{id}")
    public void updateHome(@RequestBody Post p, @PathVariable int id){postService.update(p,id);}

    @DeleteMapping(path="{id}")
    public void deleteHouseById(@PathVariable int id){postService.delete(id);}
}

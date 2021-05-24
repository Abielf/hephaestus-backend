package Olympus.Hephaestus.Controllers;

import Olympus.Hephaestus.DAO.TagService;
import Olympus.Hephaestus.Model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/tags")
@RestController
@CrossOrigin
public class TagController {
    private TagService tagService;

    @Autowired
    public TagController(TagService t){tagService=t;}

    @GetMapping
    public List<Tag> getAllTags(){return tagService.list();}

    @GetMapping(path="{id}")
    public Optional<Tag> getTagById(@PathVariable int id){return tagService.get(id);}

    @GetMapping(path="bypost/{postId}")
    public List<Tag> getTagByPost(@PathVariable int postId){return tagService.listByPost(postId);}

    @GetMapping(path="tagcloud")
    public List<String> getTagCloud(){return tagService.listLabels();}

    @PostMapping
    public int makeTag(@RequestBody Tag t){return tagService.create(t);}

    @PutMapping(path="{id}")
    public void changeTag(@RequestBody Tag t, @PathVariable int id){ tagService.update(t,id);}

    @DeleteMapping(path="{id}")
    public void deleteTag(@PathVariable int id){deleteTag(id);}

}

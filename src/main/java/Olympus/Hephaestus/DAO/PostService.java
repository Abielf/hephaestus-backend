package Olympus.Hephaestus.DAO;

import Olympus.Hephaestus.Model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements DAO<Post> {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Post> rows=(rs,rowNum)->{
        Post post=new Post();
        post.setId(rs.getInt("ID"));
        post.setTitle(rs.getString("TITLE"));
        post.setAuthor(rs.getString("AUTHOR"));
        post.setBody(rs.getString("BODY"));
        post.setPublished(rs.getDate("PUBLISHED_ON"));
    return post;
    };


    @Autowired
    public PostService(JdbcTemplate j){jdbcTemplate=j;}

    @Override
    public List<Post> list() {
        String sql="SELECT ID, TITLE, AUTHOR, BODY, PUBLISHED_ON FROM POST";
        return jdbcTemplate.query(sql, rows);
    }

    public List<Post> listByTag(String tag){
        String sql="SELECT * FROM POST INNER JOIN TAG ON POST.ID=TAG.POST_ID WHERE TAG.LABEL=?";
        return jdbcTemplate.query(sql, rows, tag);
    }


    @Override
    public int create(Post post) {
        String sql="INSERT INTO POST(TITLE, AUTHOR, BODY, PUBLISHED_ON) VALUES(?,?,?,?)";
        KeyHolder holdMeClose= new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getAuthor());
            ps.setString(3, post.getBody());
            ps.setDate(4, post.getPublished());
            return ps;
        }, holdMeClose);
        return holdMeClose.getKey().intValue();
    }

    @Override
    public Optional<Post> get(int id) {
        String sql="SELECT ID, TITLE, AUTHOR, BODY, PUBLISHED_ON FROM POST WHERE ID=?";
        Post post=null;
        try{post=jdbcTemplate.queryForObject(sql, rows, id);}
        catch(DataAccessException e){log.info("Article not found");}

        return Optional.ofNullable(post);
    }

    @Override
    public void update(Post post, int id) {
        String sql="UPDATE POST SET TITLE=?, AUTHOR=?, BODY=?, PUBLISHED_ON=? WHERE ID=?";
        int update= jdbcTemplate.update(sql, post.getTitle(), post.getAuthor(), post.getBody(),post.getPublished() ,id);
        if(update==1){log.info("Post Updated");}
    }

    @Override
    public void delete(int id) {
            jdbcTemplate.update("DELETE FROM POST WHERE ID=?",id);
    }
}

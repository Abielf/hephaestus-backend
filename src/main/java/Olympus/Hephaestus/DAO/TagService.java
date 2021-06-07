package Olympus.Hephaestus.DAO;

import Olympus.Hephaestus.Model.Post;
import Olympus.Hephaestus.Model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements DAO<Tag>{

    private static final Logger log = LoggerFactory.getLogger(TagService.class);
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Tag> rows=(rs, rowNum)->{
        Tag tag=new Tag();
        tag.setId(rs.getInt("ID"));
        tag.setLabel(rs.getString("LABEL"));
        tag.setPostId(rs.getInt("POST_ID"));
        return tag;
    };


    @Autowired
    public TagService(JdbcTemplate j){jdbcTemplate=j;}

    @Override
    public List<Tag> list() {
        String sql="SELECT ID, LABEL, POST_ID FROM TAG";
        return jdbcTemplate.query(sql,rows);
    }

    public List<String> listLabels(){
        String sql="SELECT DISTINCT LABEL FROM TAG";
        return jdbcTemplate.query(sql, (rs, rowNum)->{
            return rs.getString("LABEL");
        });
    }

    public List<Tag> listByPost(int postId){
        String sql="SELECT * FROM TAG INNER JOIN POST ON TAG.POST_ID=POST.ID WHERE POST.ID=?";
        return jdbcTemplate.query(sql,rows,postId);
    }

    @Override
    public int create(Tag tag) {
        String sql="INSERT INTO TAG(LABEL, POST_ID) VALUES(?,?)";
        KeyHolder holdMeClose= new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
            {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, tag.getLabel());
                ps.setInt(2, tag.getPostId());
                return ps;
            }
        }, holdMeClose);
        return holdMeClose.getKey().intValue();
    }

    @Override
    public Optional<Tag> get(int id) {
        String sql="SELECT ID, LABEL, POST_ID FROM TAG WHERE ID=?";
        Tag tag=null;
        try{tag=jdbcTemplate.queryForObject(sql, rows, id);}
        catch(DataAccessException e){log.info("Tag not found");}
        return Optional.ofNullable(tag);
    }

    @Override
    public void update(Tag tag, int id) {
        String sql ="UPDATE TAG SET LABEL=?, POST_ID=? WHERE ID=?";
        int update=jdbcTemplate.update(sql, tag.getLabel(),tag.getPostId(),id);
        if(update==1){log.info("Post Updated");}
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM TAG WHERE ID=?",id);
    }
}

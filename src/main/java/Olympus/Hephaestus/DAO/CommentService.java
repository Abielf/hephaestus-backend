package Olympus.Hephaestus.DAO;

import Olympus.Hephaestus.Model.Comment;
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
public class CommentService implements DAO<Comment>{

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Comment> rows=(rs, rowNum)->{
        Comment comment=new Comment();
        comment.setId(rs.getInt("ID"));
        comment.setBody(rs.getString("BODY"));
        comment.setAuthor(rs.getString("AUTHOR"));
        comment.setWrittenOn(rs.getDate("WRITTEN_ON"));
        comment.setPostId(rs.getInt("POST_ID"));
        return comment;
    };


    @Autowired
    public CommentService(JdbcTemplate j){jdbcTemplate=j;}

    @Override
    public List<Comment> list() {
        String sql="SELECT ID, BODY, AUTHOR, WRITTEN_ON, POST_ID FROM COMMENT";
        return jdbcTemplate.query(sql,rows);
    }

    public List<Comment> listByPost(int id){
        String sql="SELECT ID, BODY, AUTHOR, WRITTEN_ON, POST_ID FROM COMMENT WHERE POST_ID=?";
        return jdbcTemplate.query(sql,rows, id);
    }

    @Override
    public int create(Comment comment) {
        String sql="INSERT INTO COMMENT(BODY, AUTHOR, WRITTEN_ON, POST_ID) VALUES(?,?,?,?)";
        KeyHolder holdMeClose= new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
            {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, comment.getBody());
                ps.setString(2, comment.getAuthor());
                ps.setDate(3, comment.getWrittenOn());
                ps.setInt(4, comment.getPostId());
                return ps;
            }
        }, holdMeClose);
        return holdMeClose.getKey().intValue();
    }

    @Override
    public Optional<Comment> get(int id) {
        String sql="SELECT ID, BODY, AUTHOR, WRITTEN_ON, POST_ID FROM COMMENT WHERE ID =?";
        Comment comment=null;
        try{comment=jdbcTemplate.queryForObject(sql, rows,id);}
        catch(DataAccessException e){log.info("Comment not found");}
        return Optional.ofNullable(comment);
    }

    @Override
    public void update(Comment comment, int id) {
    String sql="UPDATE COMMENT SET BODY=?, AUTHOR=?, WRITTEN_ON=?, POST_ID=? WHERE ID = ?";
    int update=jdbcTemplate.update(sql,comment.getBody(),comment.getAuthor(),comment.getWrittenOn(),comment.getPostId(),id);
    if(update==1){log.info("Comment Updated");}
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM COMMENT WHERE ID=?",id);
    }
}

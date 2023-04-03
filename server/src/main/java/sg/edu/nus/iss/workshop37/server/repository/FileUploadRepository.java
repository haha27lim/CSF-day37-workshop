package sg.edu.nus.iss.workshop37.server.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop37.server.model.Post;

@Repository
public class FileUploadRepository {
 
    private static final String INSERT_POSTS_TBL = "INSERT INTO posts (blobc, title, complain) VALUES(?, ?, ?)";
    
    private static final String SQL_GET_POST_BY_POST_ID = 
       "select id, title, complain, blobc from posts where id=?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate template;

    // Method to upload file metadata to the database
    public void uploadBlob(MultipartFile file, String title, String complain) 
            throws SQLException, IOException  {
        
        try (Connection con = dataSource.getConnection();
            PreparedStatement pstmt = con.prepareStatement(INSERT_POSTS_TBL)) {
            // Set the binary stream for the BLOB column   
            InputStream is = file.getInputStream();
            pstmt.setBinaryStream(1, is, file.getSize()); // set the first parameter of the SQL query to the input stream from the file
            // Set the title and complain columns
            pstmt.setString(2, title); // set the second parameter of the SQL query to the title
            pstmt.setString(3, complain); // set the third parameter of the SQL query to the
            pstmt.executeUpdate(); // execute the SQL query to insert the data into the database
        }
    }

    // Method to retrieve a Post object by postId
    public Optional<Post> getPostById(Integer postId){
        return template.query(
            SQL_GET_POST_BY_POST_ID,
            (ResultSet rs) -> {
                if(!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs); // populate the post object from the result set
                return Optional.of(post);
            },
            postId
        );
    }
}

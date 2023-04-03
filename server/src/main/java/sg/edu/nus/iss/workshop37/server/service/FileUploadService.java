package sg.edu.nus.iss.workshop37.server.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop37.server.model.Post;
import sg.edu.nus.iss.workshop37.server.repository.FileUploadRepository;

@Service
public class FileUploadService {

    @Autowired
    private FileUploadRepository flRepo;

    // Method to upload a file and additional data
    public void uploadBlob(MultipartFile file, String title, String complain) 
            throws SQLException, IOException  {
        flRepo.uploadBlob(file, title, complain);
    }

    // Method to get a post by its ID
    public Optional<Post> getPostById(Integer postId){
        return flRepo.getPostById(postId);
    }
}

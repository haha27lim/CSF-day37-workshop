package sg.edu.nus.iss.workshop37.server.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import sg.edu.nus.iss.workshop37.server.model.Post;
import sg.edu.nus.iss.workshop37.server.service.FileUploadService;
import sg.edu.nus.iss.workshop37.server.service.S3Service;

@Controller
public class FileUploadController {
    
    @Autowired
    private S3Service s3svc;

    @Autowired
    private FileUploadService flSvc;

    // Constant
    private static final String BASE64_PREFIX_DECODER = "data:image/png;base64,";
        
    // Endpoint to handle Angular file uploads
    @PostMapping(path="/upload-ng", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin() // Enable Cross-Origin Resource Sharing
    public ResponseEntity<String> uploadForAngular(
        @RequestPart MultipartFile imageFile,
        @RequestPart String title,
        @RequestPart String complain
    ) throws SQLException{
        String key = "";
        System.out.printf("title: %s", title);
        System.out.printf("complain: %s", complain);
        
        try {
            key = s3svc.upload(imageFile);  // Upload file to S3 using the autowired S3Service dependency
            flSvc.uploadBlob(imageFile, title, complain); // Save file metadata to database using the autowired FileUploadService dependency
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an IO exception occurs
        }

        // Construct JSON response
        JsonObject payload = Json.createObjectBuilder()
            .add("imagekey", key)
            .build();

        return ResponseEntity.ok(payload.toString()); // Return response with JSON payload
    }

    @PostMapping(path="/upload-tf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadTF(@RequestPart MultipartFile myfile, @RequestPart String name, Model model){
        String key = "";
        // Uploading file to S3 bucket using the autowired S3Service dependency
        try {
            key = s3svc.upload(myfile);
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if an IO exception occurs
        }
        // Adding attributes to Model for template rendering
        model.addAttribute("name", name);
        model.addAttribute("file", myfile);
        model.addAttribute("key", key);
        
        return "upload"; // Return the name of the view to be rendered
    }

    @GetMapping(path="/get-image/{postId}")
    @CrossOrigin()
    public ResponseEntity<String> retrieveImage(@PathVariable Integer postId, Model model){
         // Retrieving image from database and encoding it in Base64
        Optional<Post> opt= flSvc.getPostById(postId); 
        Post p = opt.get();
        // Encode the image data retrieved from the database in Base64 format
        String encodedString = Base64.getEncoder().encodeToString(p.getImage());

        // Creating JSON object with Base64-encoded image data
        JsonObject payload = Json.createObjectBuilder()
            .add("image", BASE64_PREFIX_DECODER + encodedString)
            .build();
        return ResponseEntity.ok(payload.toString()); // Return response with JSON payload
    }
    
}

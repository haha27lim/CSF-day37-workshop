package sg.edu.nus.iss.workshop37.server.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
 
    @Autowired
    private AmazonS3 s3Client;

    @Value("${do.storage.bucket.name}")
    private String bucketName;
    

    public String upload(MultipartFile file) throws IOException{
        // Create a hashmap to store metadata about the uploaded file
        // User data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "kenneth");
        userData.put("uploadTime", new Date().toString()); // Timestamp
        userData.put("originalFilename", file.getOriginalFilename()); // Original filename
        
        // Create a new ObjectMetadata object for the file and set its properties
        ObjectMetadata metadata = new ObjectMetadata(); // Object metadata
        metadata.setContentType(file.getContentType()); // Content type
        metadata.setContentLength(file.getSize()); // File size
        metadata.setUserMetadata(userData); // Custom user metadata

        // Generates random string to create a unique file key
        String key = UUID.randomUUID().toString()
            .substring(0, 8);
        
        // Create a StringTokenizer object using the original filename of the file
        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");

        // Initialize a count variable to 0 and an empty filename extension string
        int count = 0;
        String filenameExt = "";

        // Loop through the tokenized filename and retrieve the extension
        while(tk.hasMoreTokens()){
            // If we've reached the second token, which is the filename extension, store it in the filenameExt variable and exit the loop
            if(count == 1){
                    filenameExt = tk.nextToken();
                break;
            }else{
                filenameExt = tk.nextToken();
            }
            count++;
        }
        // Print the path of the object to be uploaded to the console
        System.out.println("myobjects/%s.%s".formatted(key, filenameExt));

        // If the filename extension is "blob", append ".png" to the filenameExt string
        if(filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png";
        
        // Create a PutObjectRequest with the desired parameters
        PutObjectRequest putRequest = 
            new PutObjectRequest(
                this.bucketName, 
                "myobjects/%s.%s".formatted(key, filenameExt), 
                file.getInputStream(), 
                metadata);
        
        // Set the access control list for the uploaded file to PublicRead
        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        // Upload the file to S3 using the S3 client
        s3Client.putObject(putRequest);
        // Return the path of the uploaded object
        return "myobjects/%s.%s".formatted(key, filenameExt);
    }
}

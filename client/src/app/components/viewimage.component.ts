import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { FileuploadService } from '../services/fileupload.service';


@Component({
  selector: 'app-viewimage',
  templateUrl: './viewimage.component.html',
  styleUrls: ['./viewimage.component.css']
})
export class ViewimageComponent implements OnInit {

  postId =  ""; // initialize the postId variable to an empty string
  param$! :  Subscription;  // declare the param$ variable of type Subscription
  imageData!: any; // declare the imageData variable of type any

  constructor(private fileUploadSvc: FileuploadService,
    private activatedRoute: ActivatedRoute){

  }
  ngOnInit(): void {
    console.log("View Image Component");
    // subscribe to the activatedRoute params observable
    this.param$ = this.activatedRoute.params.subscribe(
      (params) => {
        this.postId = params['postId']; // set the postId variable to the value of the postId route
        console.log(this.postId);
        // call the getImage method of the fileUploadSvc service
        this.fileUploadSvc.getImage(this.postId).then((r)=> {
          console.log(r.image);
          // set the imageData variable to the image data
          this.imageData = r.image;
        });
      }
    );
    
  }
}

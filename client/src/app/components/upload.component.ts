import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { CameraService } from '../services/camera.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit{
    imageData!: any; // declaring imageData variable of any type
    form!: FormGroup; // declaring the form group variable
    blob!: Blob; // declaring the blob variable
    
    constructor(private router: Router, private cameraSvc: CameraService, 
        private fb:FormBuilder){

    }
    ngOnInit(): void{
      // if there is no imageData available, then navigate to the home page
      if(!this.cameraSvc.imageData) {
        this.router.navigate(['/'])
        return;
      }
      // assign imageData property to imageData from CameraService
      this.imageData = this.cameraSvc.imageData;
      // creating a FormBuilder group for title and complaint form controls
      this.form = this.fb.group(
        { title: this.fb.control<string>(''),
          complain: this.fb.control<string>(''),
        }
      )
      // converting the imageData to a Blob object and assigning it to blob property
      this.blob = this.dataURItoBlob(this.imageData)
    }

    upload(){
      const formVal = this.form.value; // get form values
      console.log(formVal);
      console.log(this.blob);
      // passing formVal and blob object to the CameraService
      this.cameraSvc.upload(formVal, this.blob)
          // if upload is successful, navigate to home page
          .then((result)=> {
            this.router.navigate(['/']);
          });
    }

    dataURItoBlob(dataURI: string){
      // extracting image data from base64 string
      var byteString = atob(dataURI.split(',')[1]);
      // extracting MIME type of image
      let mimeString = dataURI.split(',')[0].split(';')[0];

      // Creating a new ArrayBuffer and filling it with the image data
      var ab = new ArrayBuffer(byteString.length);
      var ia = new Uint8Array(ab);
      for(var i =0; i < byteString.length; i++)
        ia[i] = byteString.charCodeAt(i); // filling the array buffer with image data

      // Creating and returning a new Blob object with the image data and MIME type
      return new Blob([ab], {type: mimeString});
    }
}

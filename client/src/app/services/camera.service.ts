import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { UploadResult } from '../model/upload-result';

@Injectable({
  providedIn: 'root'
})
export class CameraService {
  // Define the imageData property
  imageData = "";

  // Define the class constructor and inject the HttpClient service
  constructor(private httpClient: HttpClient) { }

  // Define the upload method which takes a form and image as parameters
  upload(form: any, image: Blob){
    // Create a new FormData object to hold the form and image data
    const formdata = new FormData();

    // Add the form data to the FormData object
    formdata.set("title", form['title']);
    formdata.set("complain", form['complain']);
    formdata.set("imageFile", image);
    
    // Send a POST request to the server and return the response as an observable
    return firstValueFrom(this.httpClient.post<UploadResult>("/upload-ng", formdata));
  }
}

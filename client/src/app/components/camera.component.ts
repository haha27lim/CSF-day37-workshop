import { AfterViewInit, Component, OnDestroy, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { WebcamComponent, WebcamImage } from 'ngx-webcam';
import { Subject, Subscription } from 'rxjs';
import { CameraService } from '../services/camera.service';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.css']
})
export class CameraComponent implements OnDestroy, AfterViewInit{
  
  @ViewChild(WebcamComponent)
  webcam!: WebcamComponent;

  width = 400;
  height = 400;
  pics: string[]  = [] // Define pics array to hold images
  sub$!: Subscription; // Declare a subscription variable to hold imageCapture subscription
  trigger = new Subject<void>; // Define a new subject to trigger taking a picture

  // Define the constructor method
  constructor(private router: Router,private cameraSvc: CameraService){}

  // Implement the OnDestroy interface method
  ngOnDestroy(): void {
      this.sub$.unsubscribe();
  }

  // Implement the AfterViewInit interface method
  ngAfterViewInit(): void {
      // Set the trigger property of the WebcamComponent to the Subject
      this.webcam.trigger = this.trigger;
      // Subscribe to the imageCapture stream
      this.sub$ = this.webcam.imageCapture.subscribe(
        this.snapshot.bind(this)
      );
  }

  // Define the snap method to trigger taking a picture
  snap(){
    this.trigger.next();
  }

  // Define the snapshot method to handle the captured image
  snapshot (webcamImg: WebcamImage){
     // Set the imageData property of the CameraService to the image data URL
    this.cameraSvc.imageData = webcamImg.imageAsDataUrl;
    // Push the image data URL to the pics array
    this.pics.push(webcamImg.imageAsDataUrl);
  }

  
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { firstValueFrom } from 'rxjs';
import { UploadResult } from '../model/upload-result';

@Injectable({
  providedIn: 'root'
})
export class FileuploadService {

  constructor(private httpClient: HttpClient) { }

   // This method retrieves image data from the server for a given postId.
  getImage(postId: string) {
    // returns a promise that resolves with the first value emitted by the HttpClient GET request.
    return firstValueFrom(
      this.httpClient.get<UploadResult>('/get-image/' + postId)
    );
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class VideoServiceService {
    BASE_URL: string = 'https://www.googleapis.com/youtube/v3/videos?part=contentDetails';
    API_PREFIX: string = '&key=';
    API_KEY: string = 'AIzaSyDw8-YMN5RGwoMz0pack0t94U6g7XPtsOM';
    VIDEO_PREFIX: string = '&id=';

    constructor(private httpClient: HttpClient) {}

    getDuration(videoId: string): Observable<any> {
        return this.httpClient.get(this.BASE_URL + this.API_PREFIX + this.API_KEY + this.VIDEO_PREFIX + videoId);
    }
}

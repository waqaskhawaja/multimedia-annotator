import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class VideoServiceService implements OnInit {
    BASE_URL: string;
    API_PREFIX: string;
    API_KEY: string;
    VIDEO_PREFIX: string;

    constructor(private httpClient: HttpClient) {}

    getDuration(videoId: string): Observable<any> {
        return this.httpClient.get(this.BASE_URL + this.API_PREFIX + this.API_KEY + this.VIDEO_PREFIX + videoId);
    }

    ngOnInit() {
        this.BASE_URL = 'https://www.googleapis.com/youtube/v3/videos?part=contentDetails';
        this.API_PREFIX = '&key=';
        this.API_KEY = 'AIzaSyDw8-YMN5RGwoMz0pack0t94U6g7XPtsOM';
        this.VIDEO_PREFIX = '&id=';
    }
}

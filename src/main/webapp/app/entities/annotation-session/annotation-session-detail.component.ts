import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmbedVideoService } from 'ngx-embed-video';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { Options } from 'ng5-slider';

@Component({
    selector: 'jhi-annotation-session-detail',
    templateUrl: './annotation-session-detail.component.html'
})
export class AnnotationSessionDetailComponent implements OnInit {
    annotationSession: IAnnotationSession;
    analysisSessionResource: IAnalysisSessionResource;

    value: number = 100;
    videoId: string;

    options: Options = {
        floor: 0,
        ceil: 200
    };

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected annotationSessionService: AnnotationSessionService,
        protected embedService: EmbedVideoService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
        this.annotationSessionService.findVideoByAnalysisSession(this.annotationSession.analysisSession.id).subscribe(res => {
            this.analysisSessionResource = res.body;
            this.getVideoStartEnd(this.analysisSessionResource.url);
        });
    }

    embedVideo(url: string) {
        return this.embedService.embed(url);
    }

    getVideoStartEnd(url: string) {
        this.videoId = this.youtubeVideoIdFromURL(url);
        this.annotationSessionService.findVideoStatsById(this.videoId).subscribe(res => {
            console.log(res.items[0].contentDetails.duration);
        });
    }

    //  https://stackoverflow.com/questions/3452546/how-do-i-get-the-youtube-video-id-from-a-url
    youtubeVideoIdFromURL(url) {
        var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        var match = url.match(regExp);
        return match && match[7].length == 11 ? match[7] : false;
    }

    previousState() {
        window.history.back();
    }
}

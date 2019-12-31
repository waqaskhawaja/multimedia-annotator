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
    end: any;

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
            console.log(this.convertTime(res.items[0].contentDetails.duration));
        });
    }

    // https://stackoverflow.com/questions/3452546/how-do-i-get-the-youtube-video-id-from-a-url
    youtubeVideoIdFromURL(url: string) {
        let splitted = url.split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/);
        return splitted[2] !== undefined ? splitted[2].split(/[^0-9a-z_\-]/i)[0] : splitted[0];
    }

    convertTime(youtubeTime: string) {
        let durations = youtubeTime.match(/(\d+)(?=[MHS])/gi) || [];
        let miliseconds = Number(durations[0]) + 60 * 60 * 1000 + Number(durations[1]) * 60 * 1000 + Number(durations[2]) * 1000;
        let formatted = durations
            .map(function(item) {
                if (item.length < 2) {
                    return '0' + item;
                }
                return item;
            })
            .join(':');
        return miliseconds;
    }

    previousState() {
        window.history.back();
    }
}

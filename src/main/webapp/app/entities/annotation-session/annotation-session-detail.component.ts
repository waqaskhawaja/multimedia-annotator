import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmbedVideoService } from 'ngx-embed-video';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { Options, LabelType, ChangeContext } from 'ng5-slider';
import { YtPlayerService, PlayerOptions } from 'yt-player-angular';

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
        ceil: 0,
        step: 1000,
        translate: (value: number, label: LabelType): string => {
            return this.getYoutubeLikeTimeDisplay(value); // this will translate label to time stamp.
        }
    };

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected annotationSessionService: AnnotationSessionService,
        protected embedService: EmbedVideoService,
        private ytPlayerService: YtPlayerService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
        this.annotationSessionService.findVideoByAnalysisSession(this.annotationSession.analysisSession.id).subscribe(res => {
            this.analysisSessionResource = res.body;
            this.setSliderEnd(this.analysisSessionResource.url);
            this.ytPlayerService.play();
        });
        this.setSliderEnd(this.analysisSessionResource.url);
    }

    embedVideo(url: string) {
        return this.embedService.embed(url);
    }

    setSliderEnd(url: string) {
        const newOptions = Object.assign({}, this.options);
        this.videoId = this.youtubeVideoIdFromURL(this.analysisSessionResource.url);
        this.annotationSessionService.findVideoStatsById(this.videoId).subscribe(res => {
            newOptions.ceil = this.convertYoutubeVideoLengthToMiliseconds(res.items[0].contentDetails.duration);
            this.options = newOptions;
        });
    }

    // https://stackoverflow.com/questions/3452546/how-do-i-get-the-youtube-video-id-from-a-url
    youtubeVideoIdFromURL(url: string) {
        let splitted = url.split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/);
        return splitted[2] !== undefined ? splitted[2].split(/[^0-9a-z_\-]/i)[0] : splitted[0];
    }

    getYoutubeLikeTimeDisplay(millisec: number) {
        let hoursToShow: string;
        let minutesToShow: string;
        let secondsToShow: string;

        const seconds = Math.floor((millisec / 1000) % 60);
        const minutes = Math.floor((millisec / (1000 * 60)) % 60);
        const hours = Math.floor((millisec / (1000 * 60 * 60)) % 24);

        secondsToShow = String(seconds >= 10 ? seconds : '0' + seconds);

        //no hours
        if (hours < 1) {
            minutesToShow = String(minutes);
            return minutesToShow + ':' + secondsToShow;
        } else {
            minutesToShow = String(minutes >= 10 ? minutes : '0' + minutes);
            hoursToShow = String(hours);
            return hoursToShow + ':' + minutesToShow + ':' + secondsToShow;
        }
    }

    convertYoutubeVideoLengthToMiliseconds(youtubeTime: string): number {
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

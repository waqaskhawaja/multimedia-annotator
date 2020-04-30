import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmbedVideoService } from 'ngx-embed-video';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { Options, LabelType, ChangeContext } from 'ng5-slider';
import { InteractionRecordService } from 'app/entities/interaction-record';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';
import { MatTableDataSource } from '@angular/material/table';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { interval } from 'rxjs';
import { IInteractionRecordDto, InteractionRecordDto } from 'app/shared/model/interaction-record-dto.model';

@Component({
    selector: 'jhi-annotation-session-detail',
    templateUrl: './annotation-session-detail.component.html',
    animations: [
        trigger('detailExpand', [
            state('collapsed', style({ height: '0px', minHeight: '0' })),
            state('expanded', style({ height: '*' })),
            transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)'))
        ])
    ]
})
export class AnnotationSessionDetailComponent implements OnInit {
    annotationSession: IAnnotationSession;
    analysisSessionResource: IAnalysisSessionResource;
    player: YT.Player;
    private id: string;
    value: number;
    videoId: string;
    end: any;
    max: number;
    sliderEnable: boolean;
    interactionRecord: IInteractionRecord;
    text: Array<String> = [];

    initializationSlider: Boolean;
    options: Options;
    ELEMENT_DATA: IInteractionRecordDto[];
    displayedColumns: string[] = ['select', 'id', 'interaction'];
    dataSource: any;
    expandedElement: IInteractionRecord | null;
    curSec: number;
    sub: any;
    sliderStartingValue: number;
    interactionRecordDto: InteractionRecordDto[] = [];

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected annotationSessionService: AnnotationSessionService,
        protected embedService: EmbedVideoService,
        protected interactionRecordService: InteractionRecordService
    ) {
        this.sliderEnable = false;
        this.initializationSlider = true;
        this.curSec = 0;
        this.sliderStartingValue = 0;
    }

    ngOnInit() {
        this.id = 'YJ4nfAZ_ZXg';
        this.value = 0;
        this.getAllRecords();
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
        this.annotationSessionService.findVideoByAnalysisSession(this.annotationSession.analysisSession.id).subscribe(res => {
            this.analysisSessionResource = res.body;
            this.setSliderEnd(this.analysisSessionResource.url);
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
        const splitted = url.split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/);
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

        // no hours
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
        const durations = youtubeTime.match(/(\d+)(?=[MHS])/gi) || [];
        const miliseconds = Number(durations[0]) + 60 * 60 * 1000 + Number(durations[1]) * 60 * 1000 + Number(durations[2]) * 1000;
        const formatted = durations
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

    savePlayer(player) {
        this.player = player;
    }

    playVideo() {
        this.player.playVideo();
        this.max = this.player.getDuration();
        if (this.initializationSlider) {
            this.initializeSlider(this.max);
        }
        this.sliderEnable = true;
        this.startTimer(this.value);
    }

    pauseVideo() {
        this.player.pauseVideo();
        this.sub.unsubscribe();
    }

    onStateChange(event) {
        console.log('player state', event.data);
    }

    fastForward(value: number) {
        this.player.seekTo(value, true);
        this.sub.unsubscribe();
        this.value = value;
        this.sliderStartingValue = value;
        this.startTimer(this.sliderStartingValue);
        this.getDataFromAllRecord(value);
    }

    initializeSlider(maxSize: number) {
        this.options = {
            floor: 0,
            ceil: maxSize
        };
        this.initializationSlider = false;
    }

    /*
    getTextFromInteractionRecord(time: number) {
        this.interactionRecordService.findByDuration(time).subscribe(res => {
            this.ELEMENT_DATA = res.body;
            if(this.ELEMENT_DATA.length>this.check) {
                this.dataSource = new MatTableDataSource(this.ELEMENT_DATA);
                this.check= this.check+1;
            }/!*
            const inputTag = document.getElementById('text_area') as HTMLInputElement;
            if (this.interactionRecordArray != null) {
                for (let i = 0; i < this.interactionRecordArray.length; i++) {
                    inputTag.value = inputTag.value + '\n' + this.interactionRecordArray[i].interactionType.name;
                }
            }*!/
        });
    }*/

    startTimer(value: number) {
        const timer$ = interval(1000);

        this.sub = timer$.subscribe(sec => {
            if (value === 0) {
                this.value = 0 + sec;
            } else {
                this.value = value + sec;
            }
            //this.getTextFromInteractionRecord(this.value);
            this.getDataFromAllRecord(this.value);
            this.curSec = sec;

            if (this.curSec === this.max) {
                this.sub.unsubscribe();
            }
        });
    }

    getAllRecords() {
        this.interactionRecordService.getAllRecords().subscribe(res => {
            this.interactionRecordDto = res.body;
        });
    }

    getDataFromAllRecord(index: number) {
        this.ELEMENT_DATA = this.interactionRecordDto.filter(x => x.duration <= index);

        if (this.ELEMENT_DATA != null) {
            this.dataSource = new MatTableDataSource(this.ELEMENT_DATA);
        }
    }
}

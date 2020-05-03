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
import { MatCheckboxChange } from '@angular/material/checkbox';
import { Annotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from 'app/entities/annotation';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SelectionModel } from '@angular/cdk/collections';
import { HttpResponse } from '@angular/common/http';
import { IDataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSet, IDataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from 'app/entities/data-set/data-set.service';

@Component({
    selector: 'jhi-annotation-session-detail',
    styleUrls: ['./annotation-session.css'],
    templateUrl: './annotation-session-detail.component.html',
    animations: [
        trigger('detailExpand', [
            state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
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
    expandedElement: IDataSet | null;
    curSec: number;
    sub: any;
    sliderStartingValue: number;
    interactionRecordDto: InteractionRecordDto[] = [];
    firstChecked: number;
    secondChecked: number;
    firstElement: InteractionRecordDto;
    annotationToSave: Array<InteractionRecordDto> = [];
    idArray: Array<any> = [];
    selectedRowsBetweenTwoCheckBox: Array<InteractionRecordDto> = [];
    dataSets: IDataSet[];
    dataToStore: Array<InteractionRecordDto> = [];

    inLineFormEnable: Boolean;
    durationInSeconds: number = 2;
    selection = new SelectionModel<IInteractionRecordDto>(true, []);

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected annotationSessionService: AnnotationSessionService,
        protected embedService: EmbedVideoService,
        protected interactionRecordService: InteractionRecordService,
        protected annotationService: AnnotationService,
        private _snackBar: MatSnackBar,
        protected dataSetService: DataSetService
    ) {
        this.sliderEnable = false;
        this.initializationSlider = true;
        this.curSec = 0;
        this.sliderStartingValue = 0;
        this.secondChecked = 0;
        this.firstChecked = null;
        this.inLineFormEnable = false;
    }

    ngOnInit() {
        this.id = 'YJ4nfAZ_ZXg';
        this.value = 0;
        this.getAllRecords();
        this.getAllDataSet();
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
        this.annotationSessionService.findVideoByAnalysisSession(this.annotationSession.analysisSession.id).subscribe(res => {
            this.analysisSessionResource = res.body;
            //this.setSliderEnd(this.analysisSessionResource.url);
        });
        /* this.setSliderEnd(this.analysisSessionResource.url);*/
    }

    /* embedVideo(url: string) {
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
    }*/

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
        this.dataToStore = this.ELEMENT_DATA.filter(value1 => value1.interactionType.name === 'Reading');

        debugger;
        if (this.dataToStore.length > 0) {
            let sourceID = this.dataToStore[0].sourceId.split(' ').join('');
            const result = this.dataSets.filter(value1 => value1.identifier === 'ArmsDealing24');
            if (sourceID === 'Armsdealing24') {
                this.expandedElement = result[0];
                //      this.dataToStore.pop();
            }
        }
        if (this.ELEMENT_DATA != null) {
            this.dataSource = new MatTableDataSource(this.ELEMENT_DATA);
        }
    }

    getName(e: MatCheckboxChange, checkedVal: any, interactionRecord: InteractionRecordDto) {
        if (e.checked) {
            if (this.firstChecked === null) {
                this.firstChecked = checkedVal;
                this.firstElement = interactionRecord;
            } else if (this.firstChecked != null) {
                this.secondChecked = checkedVal;
                this.selectedRowsBetweenTwoCheckBox = this.ELEMENT_DATA.filter(
                    value => value.id >= this.firstElement.id && value.id <= interactionRecord.id
                );

                for (let i = 0; i < this.selectedRowsBetweenTwoCheckBox.length; i++) {
                    this.idArray.push(this.selectedRowsBetweenTwoCheckBox[i].id);
                    this.dataSource.data
                        .filter(value => value.index >= this.firstChecked && value.index <= checkedVal)
                        .forEach(var1 => this.selection.selected);
                }
            }
        } else {
            if (this.firstChecked === checkedVal) {
                this.firstChecked = null;
                this.firstElement = null;
            } else if (this.secondChecked === checkedVal) {
                alert('before del len' + this.selectedRowsBetweenTwoCheckBox.length);
                for (let k = 0; k < this.selectedRowsBetweenTwoCheckBox.length; k++) {
                    this.selectedRowsBetweenTwoCheckBox.pop();
                    alert('del elemnet' + k);
                }

                alert('after del len' + this.selectedRowsBetweenTwoCheckBox.length);

                for (let i = 0; i <= this.idArray.length; i++) {
                    this.idArray.pop();
                }
            }
        }
    }

    saveAnnotation() {
        const inputTag = document.getElementById('inputText') as HTMLInputElement;
        if (inputTag != null) {
            if (this.annotationToSave != null) {
                this.annotationService
                    .saveAnnotation(this.idArray, inputTag.value, this.annotationSessionService.getSessionValue())
                    .subscribe();
                /*  this._snackBar.open("Annotation Created", this.annotationSessionService.getSessionValue(), {
                      duration: 2000,
                  });*/
                this._snackBar.open('Annotation Created', this.annotationSessionService.getSessionValue(), {
                    duration: 4000
                });
            }
        } else {
        }
    }

    getInLineForm() {
        this.inLineFormEnable = true;
    }

    hideInLineForm() {
        this.inLineFormEnable = false;
    }

    getAllDataSet() {
        this.dataSetService.query().subscribe((res: HttpResponse<IDataSet[]>) => {
            this.dataSets = res.body;
        });
    }
}

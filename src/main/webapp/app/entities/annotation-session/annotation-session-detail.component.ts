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
import { MatSnackBar } from '@angular/material/snack-bar';
import { SelectionModel } from '@angular/cdk/collections';
import { HttpResponse } from '@angular/common/http';
import { IDataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSet, IDataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from 'app/entities/data-set/data-set.service';
import { AnnotationService } from 'app/entities/annotation/annotation.service';
import { AnnotationType, IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from 'app/entities/annotation-type/annotation-type.service';

@Component({
    selector: 'jhi-annotation-session-detail',
    styleUrls: ['./annotation-session.css'],
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
    end: any;
    max: number;
    sliderEnable: boolean;
    interactionRecord: IInteractionRecord;
    text: Array<String> = [];

    initializationSlider: Boolean;
    options: Options;
    ELEMENT_DATA: IInteractionRecordDto[];
    ELEMENT_DATA2: Array<IDataSet> = [];

    displayedColumns: string[] = ['select', 'id', 'interaction', 'text'];
    displayedColumns2 = ['contents'];
    dataSource: any;
    dataSource2: any;
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
    result: IDataSet[];
    objStore: IDataSet[];

    index: number;
    contexts: Boolean;
    inLineFormEnable: Boolean;
    durationInSeconds: number = 2;
    selection = new SelectionModel<IInteractionRecordDto>(true, []);
    storeArray: Array<IDataSet> = [];
    annotationTypes: AnnotationType[];
    annotationType: any;

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected annotationSessionService: AnnotationSessionService,
        protected embedService: EmbedVideoService,
        protected interactionRecordService: InteractionRecordService,
        protected annotationService: AnnotationService,
        private _snackBar: MatSnackBar,
        protected dataSetService: DataSetService,
        protected annotationTypeService: AnnotationTypeService
    ) {
        this.sliderEnable = false;
        this.initializationSlider = true;
        this.curSec = 0;
        this.sliderStartingValue = 0;
        this.secondChecked = 0;
        this.firstChecked = null;
        this.inLineFormEnable = false;
        this.contexts = false;
    }

    ngOnInit() {
        this.id = 'YJ4nfAZ_ZXg';
        this.value = 0;
        this.getAnnotationType();
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
        const currentTime = this.player.getCurrentTime();
        if (currentTime <= 0) {
            this.player.seekTo(1, true);
        } else {
            this.player.playVideo();
            this.player.seekTo(this.value, true);
        }
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
        const toCentiSec = index * 10;
        /*

        const toCentiSec= index*100;
*/
        this.ELEMENT_DATA = this.interactionRecordDto.filter(x => x.time <= toCentiSec);
        this.dataToStore = this.ELEMENT_DATA.filter(value1 => value1.interactionType.name === 'Reading');
        let storeArrayDataSet = new Set();

        if (this.dataToStore.length > 0) {
            for (let i = 0; i < this.dataToStore.length; i++) {
                let sourceID = this.dataToStore[i].sourceId
                    .split(' ')
                    .join('')
                    .toLowerCase();

                this.result = this.dataSets.filter(value1 => value1.identifier.toLowerCase() === sourceID);
                storeArrayDataSet.add(this.result[0]);

                if (this.result.length > 0) {
                    this.ELEMENT_DATA2 = Array.from(storeArrayDataSet);
                    this.dataSource2 = new MatTableDataSource(this.ELEMENT_DATA2);
                }
            }
        }
        if (this.ELEMENT_DATA != null) {
            this.dataSource = new MatTableDataSource(this.ELEMENT_DATA);
        }
    }

    getName(e: MatCheckboxChange, rowIndex: any, interactionRecord: InteractionRecordDto) {
        if (e.checked) {
            if (this.firstChecked === null) {
                this.firstChecked = rowIndex;
                this.firstElement = interactionRecord;
            } else if (this.firstChecked != null) {
                this.secondChecked = rowIndex;

                let secondElementSelected = interactionRecord;
                this.selectedRowsBetweenTwoCheckBox = this.ELEMENT_DATA.filter(
                    value => value.id >= this.firstElement.id && value.id <= secondElementSelected.id
                );

                for (let i = 0; i < this.selectedRowsBetweenTwoCheckBox.length; i++) {
                    this.idArray.push(this.selectedRowsBetweenTwoCheckBox[i].id);
                }

                for (let i = this.firstChecked; i <= this.secondChecked; i++) {
                    debugger;
                    if (!this.selection.isSelected(this.selectedRowsBetweenTwoCheckBox[this.firstChecked])) {
                        for (let j = 0; j < this.selectedRowsBetweenTwoCheckBox.length; j++) {
                            this.selection.select(this.selectedRowsBetweenTwoCheckBox[j]);
                        }
                    }
                }

                console.log(this.selection);
            }
        } else {
            if (this.firstChecked === rowIndex) {
                this.firstChecked = null;
                this.firstElement = null;
            } else if (this.secondChecked === rowIndex) {
                for (let i = 0; i < this.ELEMENT_DATA.length; i++) {
                    this.selection.deselect(this.ELEMENT_DATA[i]);
                }

                while (this.selectedRowsBetweenTwoCheckBox.length > 0) {
                    this.selectedRowsBetweenTwoCheckBox.pop();
                }

                while (this.idArray.length > 0) {
                    this.idArray.pop();
                }

                this.secondChecked = null;
                this.firstChecked = null;
                this.firstElement = null;
            } else {
                for (let i = 0; i < this.ELEMENT_DATA.length; i++) {
                    this.selection.deselect(this.ELEMENT_DATA[i]);
                }
            }
        }
    }

    saveAnnotation() {
        const inputTag = document.getElementById('inputText') as HTMLInputElement;
        if (inputTag != null) {
            if (this.annotationToSave != null) {
                this.annotationService
                    .saveAnnotation(this.idArray, inputTag.value, this.annotationSessionService.getSessionValue(), this.annotationType)
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

    exapndRow(e: Event, data: IInteractionRecordDto) {
        if (data.interactionType.name.toLowerCase() === 'reading') {
            this.expandedElement = this.expandedElement === data ? null : data;
        }
    }

    getAnnotationType() {
        this.annotationTypeService.query().subscribe((res: HttpResponse<IAnnotationType[]>) => {
            this.annotationTypes = res.body;
        });
    }
    trackAnnotationTypeById(index: number, item: IAnnotationType) {
        return item.id;
    }

    selectChangeHandler(event: any) {
        //update the ui
        this.annotationType = event.target.value;
    }
}

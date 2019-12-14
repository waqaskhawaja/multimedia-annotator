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
        });
    }

    embedVideo(url) {
        return this.embedService.embed(url);
    }

    previousState() {
        window.history.back();
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';

@Component({
    selector: 'jhi-annotation-session-detail',
    templateUrl: './annotation-session-detail.component.html'
})
export class AnnotationSessionDetailComponent implements OnInit {
    annotationSession: IAnnotationSession;

    constructor(protected activatedRoute: ActivatedRoute, protected annotationSessionService: AnnotationSessionService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            this.annotationSession = annotationSession;
        });
        this.annotationSessionService
            .findVideoByAnalysisSession(this.annotationSession.analysisSession.id)
            .subscribe(res => console.log(res));
    }

    previousState() {
        window.history.back();
    }
}

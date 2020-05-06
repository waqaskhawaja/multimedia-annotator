import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnalysisSessionResourceService } from 'app/entities/analysis-session-resource/analysis-session-resource.service';
import { VideoServiceService } from 'app/shared/service/video-service.service';

type EntityResponseType = HttpResponse<IAnnotationSession>;
type EntityArrayResponseType = HttpResponse<IAnnotationSession[]>;

@Injectable({ providedIn: 'root' })
export class AnnotationSessionService {
    public resourceUrl = SERVER_API_URL + 'api/annotation-sessions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/annotation-sessions';

    constructor(
        protected http: HttpClient,
        protected analysisSessionResourceService: AnalysisSessionResourceService,
        protected videoService: VideoServiceService
    ) {}

    create(annotationSession: IAnnotationSession): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(annotationSession);
        return this.http
            .post<IAnnotationSession>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(annotationSession: IAnnotationSession): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(annotationSession);
        return this.http
            .put<IAnnotationSession>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAnnotationSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findVideoByAnalysisSession(analysisSessionId: number): Observable<EntityResponseType> {
        return this.analysisSessionResourceService.findVideoByAnalysisSession(analysisSessionId);
    }

    findVideoStatsById(videoId: string) {
        return this.videoService.getDuration(videoId);
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnnotationSession[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnnotationSession[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(annotationSession: IAnnotationSession): IAnnotationSession {
        const copy: IAnnotationSession = Object.assign({}, annotationSession, {
            start: annotationSession.start != null && annotationSession.start.isValid() ? annotationSession.start.toJSON() : null,
            end: annotationSession.end != null && annotationSession.end.isValid() ? annotationSession.end.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.start = res.body.start != null ? moment(res.body.start) : null;
            res.body.end = res.body.end != null ? moment(res.body.end) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((annotationSession: IAnnotationSession) => {
                annotationSession.start = annotationSession.start != null ? moment(annotationSession.start) : null;
                annotationSession.end = annotationSession.end != null ? moment(annotationSession.end) : null;
            });
        }
        return res;
    }

    createSession(id: any) {
        sessionStorage.setItem('annotationID', id.valueOf());
    }

    getSessionValue() {
        return sessionStorage.getItem('annotationID');
    }
}

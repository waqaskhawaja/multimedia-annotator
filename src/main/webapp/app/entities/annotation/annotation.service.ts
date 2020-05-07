import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAnnotation } from 'app/shared/model/annotation.model';

type EntityResponseType = HttpResponse<IAnnotation>;
type EntityArrayResponseType = HttpResponse<IAnnotation[]>;

@Injectable({ providedIn: 'root' })
export class AnnotationService {
    public resourceUrl = SERVER_API_URL + 'api/annotations';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/annotations';
    public resourceUrlToSave = SERVER_API_URL + 'api/annotations/save';

    constructor(protected http: HttpClient) {}

    create(annotation: IAnnotation): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(annotation);
        return this.http
            .post<IAnnotation>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(annotation: IAnnotation): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(annotation);
        return this.http
            .put<IAnnotation>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAnnotation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnnotation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnnotation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(annotation: IAnnotation): IAnnotation {
        const copy: IAnnotation = Object.assign({}, annotation, {
            start: annotation.start != null && annotation.start.isValid() ? annotation.start.toJSON() : null,
            end: annotation.end != null && annotation.end.isValid() ? annotation.end.toJSON() : null
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
            res.body.forEach((annotation: IAnnotation) => {
                annotation.start = annotation.start != null ? moment(annotation.start) : null;
                annotation.end = annotation.end != null ? moment(annotation.end) : null;
            });
        }
        return res;
    }

    saveAnnotation(interactionRecordDto: any, Text: any, annotationID: any, annotationType: any): Observable<any> {
        const res = interactionRecordDto;
        return this.http.post<any>(
            `${
                this.resourceUrlToSave
            }?interactionRecordDTOS=${interactionRecordDto}&Text=${Text}&annotationID=${annotationID}&annotationType=${annotationType}`,
            { observe: 'response' }
        );
    }
}

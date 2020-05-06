import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAnnotationType } from 'app/shared/model/annotation-type.model';

type EntityResponseType = HttpResponse<IAnnotationType>;
type EntityArrayResponseType = HttpResponse<IAnnotationType[]>;

@Injectable({ providedIn: 'root' })
export class AnnotationTypeService {
    public resourceUrl = SERVER_API_URL + 'api/annotation-types';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/annotation-types';

    constructor(protected http: HttpClient) {}

    create(annotationType: IAnnotationType): Observable<EntityResponseType> {
        return this.http.post<IAnnotationType>(this.resourceUrl, annotationType, { observe: 'response' });
    }

    update(annotationType: IAnnotationType): Observable<EntityResponseType> {
        return this.http.put<IAnnotationType>(this.resourceUrl, annotationType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAnnotationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnnotationType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnnotationType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

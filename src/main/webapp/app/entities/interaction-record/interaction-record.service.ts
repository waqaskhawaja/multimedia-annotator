import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

type EntityResponseType = HttpResponse<IInteractionRecord>;
type EntityArrayResponseType = HttpResponse<IInteractionRecord[]>;

@Injectable({ providedIn: 'root' })
export class InteractionRecordService {
    public resourceUrl = SERVER_API_URL + 'api/interaction-records';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/interaction-records';

    constructor(protected http: HttpClient) {}

    create(interactionRecord: IInteractionRecord): Observable<EntityResponseType> {
        return this.http.post<IInteractionRecord>(this.resourceUrl, interactionRecord, { observe: 'response' });
    }

    update(interactionRecord: IInteractionRecord): Observable<EntityResponseType> {
        return this.http.put<IInteractionRecord>(this.resourceUrl, interactionRecord, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInteractionRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInteractionRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInteractionRecord[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

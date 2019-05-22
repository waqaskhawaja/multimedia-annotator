import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

type EntityResponseType = HttpResponse<IAnalysisSession>;
type EntityArrayResponseType = HttpResponse<IAnalysisSession[]>;

@Injectable({ providedIn: 'root' })
export class AnalysisSessionService {
    public resourceUrl = SERVER_API_URL + 'api/analysis-sessions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/analysis-sessions';

    constructor(protected http: HttpClient) {}

    create(analysisSession: IAnalysisSession): Observable<EntityResponseType> {
        return this.http.post<IAnalysisSession>(this.resourceUrl, analysisSession, { observe: 'response' });
    }

    update(analysisSession: IAnalysisSession): Observable<EntityResponseType> {
        return this.http.put<IAnalysisSession>(this.resourceUrl, analysisSession, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAnalysisSession>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisSession[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisSession[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

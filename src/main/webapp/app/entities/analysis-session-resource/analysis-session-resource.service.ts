import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';

type EntityResponseType = HttpResponse<IAnalysisSessionResource>;
type EntityArrayResponseType = HttpResponse<IAnalysisSessionResource[]>;

@Injectable({ providedIn: 'root' })
export class AnalysisSessionResourceService {
    public resourceUrl = SERVER_API_URL + 'api/analysis-session-resources';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/analysis-session-resources';

    constructor(protected http: HttpClient) {}

    create(analysisSessionResource: IAnalysisSessionResource): Observable<EntityResponseType> {
        return this.http.post<IAnalysisSessionResource>(this.resourceUrl, analysisSessionResource, { observe: 'response' });
    }

    update(analysisSessionResource: IAnalysisSessionResource): Observable<EntityResponseType> {
        return this.http.put<IAnalysisSessionResource>(this.resourceUrl, analysisSessionResource, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAnalysisSessionResource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findVideoByAnalysisSession(analysisSessionId: number): Observable<EntityResponseType> {
        return this.http.get<IAnalysisSessionResource>(`${this.resourceUrl}/video-by-analysis-session/${analysisSessionId}`, {
            observe: 'response'
        });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisSessionResource[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisSessionResource[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

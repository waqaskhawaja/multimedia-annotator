import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

type EntityResponseType = HttpResponse<IAnalysisScenario>;
type EntityArrayResponseType = HttpResponse<IAnalysisScenario[]>;

@Injectable({ providedIn: 'root' })
export class AnalysisScenarioService {
    public resourceUrl = SERVER_API_URL + 'api/analysis-scenarios';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/analysis-scenarios';

    constructor(protected http: HttpClient) {}

    create(analysisScenario: IAnalysisScenario): Observable<EntityResponseType> {
        return this.http.post<IAnalysisScenario>(this.resourceUrl, analysisScenario, { observe: 'response' });
    }

    update(analysisScenario: IAnalysisScenario): Observable<EntityResponseType> {
        return this.http.put<IAnalysisScenario>(this.resourceUrl, analysisScenario, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAnalysisScenario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisScenario[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalysisScenario[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IScenario } from 'app/shared/model/scenario.model';

type EntityResponseType = HttpResponse<IScenario>;
type EntityArrayResponseType = HttpResponse<IScenario[]>;

@Injectable({ providedIn: 'root' })
export class ScenarioService {
    public resourceUrl = SERVER_API_URL + 'api/scenarios';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/scenarios';

    constructor(protected http: HttpClient) {}

    create(scenario: IScenario): Observable<EntityResponseType> {
        return this.http.post<IScenario>(this.resourceUrl, scenario, { observe: 'response' });
    }

    update(scenario: IScenario): Observable<EntityResponseType> {
        return this.http.put<IScenario>(this.resourceUrl, scenario, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IScenario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IScenario[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IScenario[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

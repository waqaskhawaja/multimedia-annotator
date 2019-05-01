import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnalyst } from 'app/shared/model/analyst.model';

type EntityResponseType = HttpResponse<IAnalyst>;
type EntityArrayResponseType = HttpResponse<IAnalyst[]>;

@Injectable({ providedIn: 'root' })
export class AnalystService {
    public resourceUrl = SERVER_API_URL + 'api/analysts';

    constructor(protected http: HttpClient) {}

    create(analyst: IAnalyst): Observable<EntityResponseType> {
        return this.http.post<IAnalyst>(this.resourceUrl, analyst, { observe: 'response' });
    }

    update(analyst: IAnalyst): Observable<EntityResponseType> {
        return this.http.put<IAnalyst>(this.resourceUrl, analyst, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAnalyst>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAnalyst[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

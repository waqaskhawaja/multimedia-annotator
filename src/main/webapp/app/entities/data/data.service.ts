import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IData } from 'app/shared/model/data.model';

type EntityResponseType = HttpResponse<IData>;
type EntityArrayResponseType = HttpResponse<IData[]>;

@Injectable({ providedIn: 'root' })
export class DataService {
    public resourceUrl = SERVER_API_URL + 'api/data';

    constructor(protected http: HttpClient) {}

    create(data: IData): Observable<EntityResponseType> {
        return this.http.post<IData>(this.resourceUrl, data, { observe: 'response' });
    }

    update(data: IData): Observable<EntityResponseType> {
        return this.http.put<IData>(this.resourceUrl, data, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IData[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

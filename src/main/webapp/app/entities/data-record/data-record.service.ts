import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDataRecord } from 'app/shared/model/data-record.model';

type EntityResponseType = HttpResponse<IDataRecord>;
type EntityArrayResponseType = HttpResponse<IDataRecord[]>;

@Injectable({ providedIn: 'root' })
export class DataRecordService {
    public resourceUrl = SERVER_API_URL + 'api/data-records';

    constructor(protected http: HttpClient) {}

    create(dataRecord: IDataRecord): Observable<EntityResponseType> {
        return this.http.post<IDataRecord>(this.resourceUrl, dataRecord, { observe: 'response' });
    }

    update(dataRecord: IDataRecord): Observable<EntityResponseType> {
        return this.http.put<IDataRecord>(this.resourceUrl, dataRecord, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDataRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDataRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

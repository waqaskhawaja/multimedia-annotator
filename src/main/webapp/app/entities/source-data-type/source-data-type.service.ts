import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISourceDataType } from 'app/shared/model/source-data-type.model';

type EntityResponseType = HttpResponse<ISourceDataType>;
type EntityArrayResponseType = HttpResponse<ISourceDataType[]>;

@Injectable({ providedIn: 'root' })
export class SourceDataTypeService {
    public resourceUrl = SERVER_API_URL + 'api/source-data-types';

    constructor(protected http: HttpClient) {}

    create(sourceDataType: ISourceDataType): Observable<EntityResponseType> {
        return this.http.post<ISourceDataType>(this.resourceUrl, sourceDataType, { observe: 'response' });
    }

    update(sourceDataType: ISourceDataType): Observable<EntityResponseType> {
        return this.http.put<ISourceDataType>(this.resourceUrl, sourceDataType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISourceDataType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISourceDataType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

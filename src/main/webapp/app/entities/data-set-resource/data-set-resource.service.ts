import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDataSetResource } from 'app/shared/model/data-set-resource.model';

type EntityResponseType = HttpResponse<IDataSetResource>;
type EntityArrayResponseType = HttpResponse<IDataSetResource[]>;

@Injectable({ providedIn: 'root' })
export class DataSetResourceService {
    public resourceUrl = SERVER_API_URL + 'api/data-set-resources';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/data-set-resources';

    constructor(protected http: HttpClient) {}

    create(dataSetResource: IDataSetResource): Observable<EntityResponseType> {
        return this.http.post<IDataSetResource>(this.resourceUrl, dataSetResource, { observe: 'response' });
    }

    update(dataSetResource: IDataSetResource): Observable<EntityResponseType> {
        return this.http.put<IDataSetResource>(this.resourceUrl, dataSetResource, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDataSetResource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDataSetResource[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDataSetResource[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

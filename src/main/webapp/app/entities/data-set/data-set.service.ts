import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDataSet } from 'app/shared/model/data-set.model';

type EntityResponseType = HttpResponse<IDataSet>;
type EntityArrayResponseType = HttpResponse<IDataSet[]>;

@Injectable({ providedIn: 'root' })
export class DataSetService {
    public resourceUrl = SERVER_API_URL + 'api/data-sets';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/data-sets';

    constructor(protected http: HttpClient) {}

    create(dataSet: IDataSet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dataSet);
        return this.http
            .post<IDataSet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(dataSet: IDataSet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dataSet);
        return this.http
            .put<IDataSet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDataSet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDataSet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDataSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(dataSet: IDataSet): IDataSet {
        const copy: IDataSet = Object.assign({}, dataSet, {
            date: dataSet.date != null && dataSet.date.isValid() ? dataSet.date.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.date = res.body.date != null ? moment(res.body.date) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((dataSet: IDataSet) => {
                dataSet.date = dataSet.date != null ? moment(dataSet.date) : null;
            });
        }
        return res;
    }
}

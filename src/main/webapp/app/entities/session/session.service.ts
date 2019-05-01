import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISession } from 'app/shared/model/session.model';

type EntityResponseType = HttpResponse<ISession>;
type EntityArrayResponseType = HttpResponse<ISession[]>;

@Injectable({ providedIn: 'root' })
export class SessionService {
    public resourceUrl = SERVER_API_URL + 'api/sessions';

    constructor(protected http: HttpClient) {}

    create(session: ISession): Observable<EntityResponseType> {
        return this.http.post<ISession>(this.resourceUrl, session, { observe: 'response' });
    }

    update(session: ISession): Observable<EntityResponseType> {
        return this.http.put<ISession>(this.resourceUrl, session, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISession>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISession[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

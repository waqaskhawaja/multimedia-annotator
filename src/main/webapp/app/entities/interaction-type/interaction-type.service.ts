import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInteractionType } from 'app/shared/model/interaction-type.model';

type EntityResponseType = HttpResponse<IInteractionType>;
type EntityArrayResponseType = HttpResponse<IInteractionType[]>;

@Injectable({ providedIn: 'root' })
export class InteractionTypeService {
    public resourceUrl = SERVER_API_URL + 'api/interaction-types';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/interaction-types';

    constructor(protected http: HttpClient) {}

    create(interactionType: IInteractionType): Observable<EntityResponseType> {
        return this.http.post<IInteractionType>(this.resourceUrl, interactionType, { observe: 'response' });
    }

    update(interactionType: IInteractionType): Observable<EntityResponseType> {
        return this.http.put<IInteractionType>(this.resourceUrl, interactionType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInteractionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInteractionType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInteractionType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

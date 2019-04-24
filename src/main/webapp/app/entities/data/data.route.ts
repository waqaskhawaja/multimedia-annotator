import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Data } from 'app/shared/model/data.model';
import { DataService } from './data.service';
import { DataComponent } from './data.component';
import { DataDetailComponent } from './data-detail.component';
import { DataUpdateComponent } from './data-update.component';
import { DataDeletePopupComponent } from './data-delete-dialog.component';
import { IData } from 'app/shared/model/data.model';

@Injectable({ providedIn: 'root' })
export class DataResolve implements Resolve<IData> {
    constructor(private service: DataService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IData> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Data>) => response.ok),
                map((data: HttpResponse<Data>) => data.body)
            );
        }
        return of(new Data());
    }
}

export const dataRoute: Routes = [
    {
        path: '',
        component: DataComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.data.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DataDetailComponent,
        resolve: {
            data: DataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.data.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DataUpdateComponent,
        resolve: {
            data: DataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.data.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DataUpdateComponent,
        resolve: {
            data: DataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.data.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dataPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DataDeletePopupComponent,
        resolve: {
            data: DataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.data.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

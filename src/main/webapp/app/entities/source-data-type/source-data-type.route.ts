import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SourceDataType } from 'app/shared/model/source-data-type.model';
import { SourceDataTypeService } from './source-data-type.service';
import { SourceDataTypeComponent } from './source-data-type.component';
import { SourceDataTypeDetailComponent } from './source-data-type-detail.component';
import { SourceDataTypeUpdateComponent } from './source-data-type-update.component';
import { SourceDataTypeDeletePopupComponent } from './source-data-type-delete-dialog.component';
import { ISourceDataType } from 'app/shared/model/source-data-type.model';

@Injectable({ providedIn: 'root' })
export class SourceDataTypeResolve implements Resolve<ISourceDataType> {
    constructor(private service: SourceDataTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISourceDataType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SourceDataType>) => response.ok),
                map((sourceDataType: HttpResponse<SourceDataType>) => sourceDataType.body)
            );
        }
        return of(new SourceDataType());
    }
}

export const sourceDataTypeRoute: Routes = [
    {
        path: '',
        component: SourceDataTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.sourceDataType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SourceDataTypeDetailComponent,
        resolve: {
            sourceDataType: SourceDataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.sourceDataType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SourceDataTypeUpdateComponent,
        resolve: {
            sourceDataType: SourceDataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.sourceDataType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SourceDataTypeUpdateComponent,
        resolve: {
            sourceDataType: SourceDataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.sourceDataType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sourceDataTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SourceDataTypeDeletePopupComponent,
        resolve: {
            sourceDataType: SourceDataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.sourceDataType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

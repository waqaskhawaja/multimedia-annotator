import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DataType } from 'app/shared/model/data-type.model';
import { DataTypeService } from './data-type.service';
import { DataTypeComponent } from './data-type.component';
import { DataTypeDetailComponent } from './data-type-detail.component';
import { DataTypeUpdateComponent } from './data-type-update.component';
import { DataTypeDeletePopupComponent } from './data-type-delete-dialog.component';
import { IDataType } from 'app/shared/model/data-type.model';

@Injectable({ providedIn: 'root' })
export class DataTypeResolve implements Resolve<IDataType> {
    constructor(private service: DataTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDataType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DataType>) => response.ok),
                map((dataType: HttpResponse<DataType>) => dataType.body)
            );
        }
        return of(new DataType());
    }
}

export const dataTypeRoute: Routes = [
    {
        path: '',
        component: DataTypeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'DataTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DataTypeDetailComponent,
        resolve: {
            dataType: DataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DataTypeUpdateComponent,
        resolve: {
            dataType: DataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DataTypeUpdateComponent,
        resolve: {
            dataType: DataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dataTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DataTypeDeletePopupComponent,
        resolve: {
            dataType: DataTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

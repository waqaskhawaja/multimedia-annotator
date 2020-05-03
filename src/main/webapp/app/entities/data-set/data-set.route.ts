import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from './data-set.service';
import { DataSetComponent } from './data-set.component';
import { DataSetDetailComponent } from './data-set-detail.component';
import { DataSetUpdateComponent } from './data-set-update.component';
import { IDataSet } from 'app/shared/model/data-set.model';

@Injectable({ providedIn: 'root' })
export class DataSetResolve implements Resolve<IDataSet> {
    constructor(private service: DataSetService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<IDataSet> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(map((dataSet: HttpResponse<DataSet>) => dataSet.body));
        }
        return of(new DataSet());
    }
}

export const dataSetRoute: Routes = [
    {
        path: '',
        component: DataSetComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DataSetDetailComponent,
        resolve: {
            dataSet: DataSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DataSetUpdateComponent,
        resolve: {
            dataSet: DataSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DataSetUpdateComponent,
        resolve: {
            dataSet: DataSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSets'
        },
        canActivate: [UserRouteAccessService]
    }
];

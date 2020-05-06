import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSetResourceService } from './data-set-resource.service';
import { DataSetResourceComponent } from './data-set-resource.component';
import { DataSetResourceDetailComponent } from './data-set-resource-detail.component';
import { DataSetResourceUpdateComponent } from './data-set-resource-update.component';
import { IDataSetResource } from 'app/shared/model/data-set-resource.model';

@Injectable({ providedIn: 'root' })
export class DataSetResourceResolve implements Resolve<IDataSetResource> {
    constructor(private service: DataSetResourceService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<IDataSetResource> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(map((dataSetResource: HttpResponse<DataSetResource>) => dataSetResource.body));
        }
        return of(new DataSetResource());
    }
}

export const dataSetResourceRoute: Routes = [
    {
        path: '',
        component: DataSetResourceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSetResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DataSetResourceDetailComponent,
        resolve: {
            dataSetResource: DataSetResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSetResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DataSetResourceUpdateComponent,
        resolve: {
            dataSetResource: DataSetResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSetResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DataSetResourceUpdateComponent,
        resolve: {
            dataSetResource: DataSetResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DataSetResources'
        },
        canActivate: [UserRouteAccessService]
    }
];

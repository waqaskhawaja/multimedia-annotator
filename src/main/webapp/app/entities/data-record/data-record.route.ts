import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DataRecord } from 'app/shared/model/data-record.model';
import { DataRecordService } from './data-record.service';
import { DataRecordComponent } from './data-record.component';
import { DataRecordDetailComponent } from './data-record-detail.component';
import { DataRecordUpdateComponent } from './data-record-update.component';
import { DataRecordDeletePopupComponent } from './data-record-delete-dialog.component';
import { IDataRecord } from 'app/shared/model/data-record.model';

@Injectable({ providedIn: 'root' })
export class DataRecordResolve implements Resolve<IDataRecord> {
    constructor(private service: DataRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDataRecord> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DataRecord>) => response.ok),
                map((dataRecord: HttpResponse<DataRecord>) => dataRecord.body)
            );
        }
        return of(new DataRecord());
    }
}

export const dataRecordRoute: Routes = [
    {
        path: '',
        component: DataRecordComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.dataRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DataRecordDetailComponent,
        resolve: {
            dataRecord: DataRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.dataRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DataRecordUpdateComponent,
        resolve: {
            dataRecord: DataRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.dataRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DataRecordUpdateComponent,
        resolve: {
            dataRecord: DataRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.dataRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dataRecordPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DataRecordDeletePopupComponent,
        resolve: {
            dataRecord: DataRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.dataRecord.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

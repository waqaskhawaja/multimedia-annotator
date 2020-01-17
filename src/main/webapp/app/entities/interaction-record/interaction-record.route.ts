import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InteractionRecord } from 'app/shared/model/interaction-record.model';
import { InteractionRecordService } from './interaction-record.service';
import { InteractionRecordComponent } from './interaction-record.component';
import { InteractionRecordDetailComponent } from './interaction-record-detail.component';
import { InteractionRecordUpdateComponent } from './interaction-record-update.component';
import { InteractionRecordDeletePopupComponent } from './interaction-record-delete-dialog.component';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';

@Injectable({ providedIn: 'root' })
export class InteractionRecordResolve implements Resolve<IInteractionRecord> {
    constructor(private service: InteractionRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInteractionRecord> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<InteractionRecord>) => response.ok),
                map((interactionRecord: HttpResponse<InteractionRecord>) => interactionRecord.body)
            );
        }
        return of(new InteractionRecord());
    }
}

export const interactionRecordRoute: Routes = [
    {
        path: '',
        component: InteractionRecordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'InteractionRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: InteractionRecordDetailComponent,
        resolve: {
            interactionRecord: InteractionRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: InteractionRecordUpdateComponent,
        resolve: {
            interactionRecord: InteractionRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: InteractionRecordUpdateComponent,
        resolve: {
            interactionRecord: InteractionRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionRecords'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const interactionRecordPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: InteractionRecordDeletePopupComponent,
        resolve: {
            interactionRecord: InteractionRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionRecords'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

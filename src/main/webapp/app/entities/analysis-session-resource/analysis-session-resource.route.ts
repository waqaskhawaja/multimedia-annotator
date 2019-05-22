import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { AnalysisSessionResourceService } from './analysis-session-resource.service';
import { AnalysisSessionResourceComponent } from './analysis-session-resource.component';
import { AnalysisSessionResourceDetailComponent } from './analysis-session-resource-detail.component';
import { AnalysisSessionResourceUpdateComponent } from './analysis-session-resource-update.component';
import { AnalysisSessionResourceDeletePopupComponent } from './analysis-session-resource-delete-dialog.component';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';

@Injectable({ providedIn: 'root' })
export class AnalysisSessionResourceResolve implements Resolve<IAnalysisSessionResource> {
    constructor(private service: AnalysisSessionResourceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnalysisSessionResource> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AnalysisSessionResource>) => response.ok),
                map((analysisSessionResource: HttpResponse<AnalysisSessionResource>) => analysisSessionResource.body)
            );
        }
        return of(new AnalysisSessionResource());
    }
}

export const analysisSessionResourceRoute: Routes = [
    {
        path: '',
        component: AnalysisSessionResourceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AnalysisSessionResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnalysisSessionResourceDetailComponent,
        resolve: {
            analysisSessionResource: AnalysisSessionResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessionResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnalysisSessionResourceUpdateComponent,
        resolve: {
            analysisSessionResource: AnalysisSessionResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessionResources'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnalysisSessionResourceUpdateComponent,
        resolve: {
            analysisSessionResource: AnalysisSessionResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessionResources'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const analysisSessionResourcePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnalysisSessionResourceDeletePopupComponent,
        resolve: {
            analysisSessionResource: AnalysisSessionResourceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessionResources'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

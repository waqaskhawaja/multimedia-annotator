import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnalysisSession } from 'app/shared/model/analysis-session.model';
import { AnalysisSessionService } from './analysis-session.service';
import { AnalysisSessionComponent } from './analysis-session.component';
import { AnalysisSessionDetailComponent } from './analysis-session-detail.component';
import { AnalysisSessionUpdateComponent } from './analysis-session-update.component';
import { AnalysisSessionDeletePopupComponent } from './analysis-session-delete-dialog.component';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';

@Injectable({ providedIn: 'root' })
export class AnalysisSessionResolve implements Resolve<IAnalysisSession> {
    constructor(private service: AnalysisSessionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnalysisSession> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AnalysisSession>) => response.ok),
                map((analysisSession: HttpResponse<AnalysisSession>) => analysisSession.body)
            );
        }
        return of(new AnalysisSession());
    }
}

export const analysisSessionRoute: Routes = [
    {
        path: '',
        component: AnalysisSessionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AnalysisSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnalysisSessionDetailComponent,
        resolve: {
            analysisSession: AnalysisSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnalysisSessionUpdateComponent,
        resolve: {
            analysisSession: AnalysisSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnalysisSessionUpdateComponent,
        resolve: {
            analysisSession: AnalysisSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const analysisSessionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnalysisSessionDeletePopupComponent,
        resolve: {
            analysisSession: AnalysisSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisSessions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

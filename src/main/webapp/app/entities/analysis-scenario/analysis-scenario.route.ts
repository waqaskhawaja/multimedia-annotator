import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';
import { AnalysisScenarioComponent } from './analysis-scenario.component';
import { AnalysisScenarioDetailComponent } from './analysis-scenario-detail.component';
import { AnalysisScenarioUpdateComponent } from './analysis-scenario-update.component';
import { AnalysisScenarioDeletePopupComponent } from './analysis-scenario-delete-dialog.component';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

@Injectable({ providedIn: 'root' })
export class AnalysisScenarioResolve implements Resolve<IAnalysisScenario> {
    constructor(private service: AnalysisScenarioService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnalysisScenario> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AnalysisScenario>) => response.ok),
                map((analysisScenario: HttpResponse<AnalysisScenario>) => analysisScenario.body)
            );
        }
        return of(new AnalysisScenario());
    }
}

export const analysisScenarioRoute: Routes = [
    {
        path: '',
        component: AnalysisScenarioComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AnalysisScenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnalysisScenarioDetailComponent,
        resolve: {
            analysisScenario: AnalysisScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisScenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnalysisScenarioUpdateComponent,
        resolve: {
            analysisScenario: AnalysisScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisScenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnalysisScenarioUpdateComponent,
        resolve: {
            analysisScenario: AnalysisScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisScenarios'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const analysisScenarioPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnalysisScenarioDeletePopupComponent,
        resolve: {
            analysisScenario: AnalysisScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnalysisScenarios'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

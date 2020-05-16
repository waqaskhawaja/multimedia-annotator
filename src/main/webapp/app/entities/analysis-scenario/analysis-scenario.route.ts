import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';
import { AnalysisScenarioComponent } from './analysis-scenario.component';
import { AnalysisScenarioDetailComponent } from './analysis-scenario-detail.component';
import { AnalysisScenarioUpdateComponent } from './analysis-scenario-update.component';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

@Injectable({ providedIn: 'root' })
export class AnalysisScenarioResolve implements Resolve<IAnalysisScenario> {
    constructor(private service: AnalysisScenarioService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<IAnalysisScenario> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(map((analysisScenario: HttpResponse<AnalysisScenario>) => analysisScenario.body));
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

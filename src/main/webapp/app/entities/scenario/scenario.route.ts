import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Scenario } from 'app/shared/model/scenario.model';
import { ScenarioService } from './scenario.service';
import { ScenarioComponent } from './scenario.component';
import { ScenarioDetailComponent } from './scenario-detail.component';
import { ScenarioUpdateComponent } from './scenario-update.component';
import { ScenarioDeletePopupComponent } from './scenario-delete-dialog.component';
import { IScenario } from 'app/shared/model/scenario.model';

@Injectable({ providedIn: 'root' })
export class ScenarioResolve implements Resolve<IScenario> {
    constructor(private service: ScenarioService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IScenario> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Scenario>) => response.ok),
                map((scenario: HttpResponse<Scenario>) => scenario.body)
            );
        }
        return of(new Scenario());
    }
}

export const scenarioRoute: Routes = [
    {
        path: '',
        component: ScenarioComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Scenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ScenarioDetailComponent,
        resolve: {
            scenario: ScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ScenarioUpdateComponent,
        resolve: {
            scenario: ScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scenarios'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ScenarioUpdateComponent,
        resolve: {
            scenario: ScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scenarios'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const scenarioPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ScenarioDeletePopupComponent,
        resolve: {
            scenario: ScenarioResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Scenarios'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

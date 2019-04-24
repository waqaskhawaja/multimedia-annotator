import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Analyst } from 'app/shared/model/analyst.model';
import { AnalystService } from './analyst.service';
import { AnalystComponent } from './analyst.component';
import { AnalystDetailComponent } from './analyst-detail.component';
import { AnalystUpdateComponent } from './analyst-update.component';
import { AnalystDeletePopupComponent } from './analyst-delete-dialog.component';
import { IAnalyst } from 'app/shared/model/analyst.model';

@Injectable({ providedIn: 'root' })
export class AnalystResolve implements Resolve<IAnalyst> {
    constructor(private service: AnalystService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnalyst> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Analyst>) => response.ok),
                map((analyst: HttpResponse<Analyst>) => analyst.body)
            );
        }
        return of(new Analyst());
    }
}

export const analystRoute: Routes = [
    {
        path: '',
        component: AnalystComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.analyst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnalystDetailComponent,
        resolve: {
            analyst: AnalystResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.analyst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnalystUpdateComponent,
        resolve: {
            analyst: AnalystResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.analyst.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnalystUpdateComponent,
        resolve: {
            analyst: AnalystResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.analyst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const analystPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnalystDeletePopupComponent,
        resolve: {
            analyst: AnalystResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'multimediaAnnotatorApp.analyst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

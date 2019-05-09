import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';
import { AnnotationSessionComponent } from './annotation-session.component';
import { AnnotationSessionDetailComponent } from './annotation-session-detail.component';
import { AnnotationSessionUpdateComponent } from './annotation-session-update.component';
import { AnnotationSessionDeletePopupComponent } from './annotation-session-delete-dialog.component';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';

@Injectable({ providedIn: 'root' })
export class AnnotationSessionResolve implements Resolve<IAnnotationSession> {
    constructor(private service: AnnotationSessionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnnotationSession> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AnnotationSession>) => response.ok),
                map((annotationSession: HttpResponse<AnnotationSession>) => annotationSession.body)
            );
        }
        return of(new AnnotationSession());
    }
}

export const annotationSessionRoute: Routes = [
    {
        path: '',
        component: AnnotationSessionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AnnotationSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnnotationSessionDetailComponent,
        resolve: {
            annotationSession: AnnotationSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnnotationSessionUpdateComponent,
        resolve: {
            annotationSession: AnnotationSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationSessions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnnotationSessionUpdateComponent,
        resolve: {
            annotationSession: AnnotationSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationSessions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const annotationSessionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnnotationSessionDeletePopupComponent,
        resolve: {
            annotationSession: AnnotationSessionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationSessions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

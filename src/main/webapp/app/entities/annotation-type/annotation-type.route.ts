import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';
import { AnnotationTypeComponent } from './annotation-type.component';
import { AnnotationTypeDetailComponent } from './annotation-type-detail.component';
import { AnnotationTypeUpdateComponent } from './annotation-type-update.component';
import { AnnotationTypeDeletePopupComponent } from './annotation-type-delete-dialog.component';
import { IAnnotationType } from 'app/shared/model/annotation-type.model';

@Injectable({ providedIn: 'root' })
export class AnnotationTypeResolve implements Resolve<IAnnotationType> {
    constructor(private service: AnnotationTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnnotationType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<AnnotationType>) => response.ok),
                map((annotationType: HttpResponse<AnnotationType>) => annotationType.body)
            );
        }
        return of(new AnnotationType());
    }
}

export const annotationTypeRoute: Routes = [
    {
        path: '',
        component: AnnotationTypeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'AnnotationTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnnotationTypeDetailComponent,
        resolve: {
            annotationType: AnnotationTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnnotationTypeUpdateComponent,
        resolve: {
            annotationType: AnnotationTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnnotationTypeUpdateComponent,
        resolve: {
            annotationType: AnnotationTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const annotationTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AnnotationTypeDeletePopupComponent,
        resolve: {
            annotationType: AnnotationTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'AnnotationTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

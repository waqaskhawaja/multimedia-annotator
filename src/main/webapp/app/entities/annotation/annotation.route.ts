import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Annotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from './annotation.service';
import { AnnotationComponent } from './annotation.component';
import { AnnotationDetailComponent } from './annotation-detail.component';
import { AnnotationUpdateComponent } from './annotation-update.component';
import { IAnnotation } from 'app/shared/model/annotation.model';

@Injectable({ providedIn: 'root' })
export class AnnotationResolve implements Resolve<IAnnotation> {
    constructor(private service: AnnotationService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<IAnnotation> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(map((annotation: HttpResponse<Annotation>) => annotation.body));
        }
        return of(new Annotation());
    }
}

export const annotationRoute: Routes = [
    {
        path: '',
        component: AnnotationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Annotations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AnnotationDetailComponent,
        resolve: {
            annotation: AnnotationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Annotations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AnnotationUpdateComponent,
        resolve: {
            annotation: AnnotationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Annotations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: AnnotationUpdateComponent,
        resolve: {
            annotation: AnnotationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Annotations'
        },
        canActivate: [UserRouteAccessService]
    }
];

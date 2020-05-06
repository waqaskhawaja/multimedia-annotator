import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';
import { AnnotationTypeComponent } from './annotation-type.component';
import { AnnotationTypeDetailComponent } from './annotation-type-detail.component';
import { AnnotationTypeUpdateComponent } from './annotation-type-update.component';
import { IAnnotationType } from 'app/shared/model/annotation-type.model';

@Injectable({ providedIn: 'root' })
export class AnnotationTypeResolve implements Resolve<IAnnotationType> {
    constructor(private service: AnnotationTypeService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<IAnnotationType> {
        const id = route.params['id'];
        if (id) {
            return this.service.find(id).pipe(map((annotationType: HttpResponse<AnnotationType>) => annotationType.body));
        }
        return of(new AnnotationType());
    }
}

export const annotationTypeRoute: Routes = [
    {
        path: '',
        component: AnnotationTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
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

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InteractionType } from 'app/shared/model/interaction-type.model';
import { InteractionTypeService } from './interaction-type.service';
import { InteractionTypeComponent } from './interaction-type.component';
import { InteractionTypeDetailComponent } from './interaction-type-detail.component';
import { InteractionTypeUpdateComponent } from './interaction-type-update.component';
import { InteractionTypeDeletePopupComponent } from './interaction-type-delete-dialog.component';
import { IInteractionType } from 'app/shared/model/interaction-type.model';

@Injectable({ providedIn: 'root' })
export class InteractionTypeResolve implements Resolve<IInteractionType> {
    constructor(private service: InteractionTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInteractionType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<InteractionType>) => response.ok),
                map((interactionType: HttpResponse<InteractionType>) => interactionType.body)
            );
        }
        return of(new InteractionType());
    }
}

export const interactionTypeRoute: Routes = [
    {
        path: '',
        component: InteractionTypeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'InteractionTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: InteractionTypeDetailComponent,
        resolve: {
            interactionType: InteractionTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: InteractionTypeUpdateComponent,
        resolve: {
            interactionType: InteractionTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionTypes'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: InteractionTypeUpdateComponent,
        resolve: {
            interactionType: InteractionTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const interactionTypePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: InteractionTypeDeletePopupComponent,
        resolve: {
            interactionType: InteractionTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'InteractionTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

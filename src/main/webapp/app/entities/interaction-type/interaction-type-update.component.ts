import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { InteractionTypeService } from './interaction-type.service';

@Component({
    selector: 'jhi-interaction-type-update',
    templateUrl: './interaction-type-update.component.html'
})
export class InteractionTypeUpdateComponent implements OnInit {
    interactionType: IInteractionType;
    isSaving: boolean;

    constructor(protected interactionTypeService: InteractionTypeService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ interactionType }) => {
            this.interactionType = interactionType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.interactionType.id !== undefined) {
            this.subscribeToSaveResponse(this.interactionTypeService.update(this.interactionType));
        } else {
            this.subscribeToSaveResponse(this.interactionTypeService.create(this.interactionType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IInteractionType>>) {
        result.subscribe((res: HttpResponse<IInteractionType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}

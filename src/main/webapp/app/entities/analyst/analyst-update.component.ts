import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IAnalyst } from 'app/shared/model/analyst.model';
import { AnalystService } from './analyst.service';

@Component({
    selector: 'jhi-analyst-update',
    templateUrl: './analyst-update.component.html'
})
export class AnalystUpdateComponent implements OnInit {
    analyst: IAnalyst;
    isSaving: boolean;

    constructor(protected analystService: AnalystService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ analyst }) => {
            this.analyst = analyst;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.analyst.id !== undefined) {
            this.subscribeToSaveResponse(this.analystService.update(this.analyst));
        } else {
            this.subscribeToSaveResponse(this.analystService.create(this.analyst));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalyst>>) {
        result.subscribe((res: HttpResponse<IAnalyst>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
